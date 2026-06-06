package com.strongBeton.strongBeton.service.workout;

import com.strongBeton.strongBeton.dto.workout.ExerciseDTO;
import com.strongBeton.strongBeton.dto.workout.WorkoutDTO;
import com.strongBeton.strongBeton.dto.workout.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.entity.coach.ClientCoach;
import com.strongBeton.strongBeton.entity.coach.Coach;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.entity.workout.*;
import com.strongBeton.strongBeton.enums.CoachStatus;
import com.strongBeton.strongBeton.enums.WorkoutStatus;
import com.strongBeton.strongBeton.service.clan.ClanContributionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private static final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);
    ModelMapper modelMapper;
    WorkoutDetailsRepository workoutDetailsRepository;
    WorkoutRepository workoutRepository;
    SetsRepository setsRepository;
    ExerciseRepository exerciseRepository;
    MuscleGroupRepository muscleGroupRepository;
    UserRepository userRepository;
    CoachRepository coachRepository;
    ClientCoachRepository clientCoachRepository;
    private final ClanContributionService clanContributionService;

    public WorkoutServiceImpl(ModelMapper modelMapper,
                              WorkoutDetailsRepository workoutDetailsRepository,
                              WorkoutRepository workoutRepository,
                              SetsRepository setsRepository,
                              UserRepository userRepository,
                              ExerciseRepository exerciseRepository,
                              MuscleGroupRepository muscleGroupRepository,
                              CoachRepository coachRepository,
                              ClientCoachRepository clientCoachRepository,
                              ClanContributionService clanContributionService) {
        this.modelMapper = modelMapper;
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.workoutRepository = workoutRepository;
        this.setsRepository = setsRepository;
        this.userRepository = userRepository;
        this.muscleGroupRepository = muscleGroupRepository;
        this.exerciseRepository = exerciseRepository;
        this.coachRepository = coachRepository;
        this.clientCoachRepository = clientCoachRepository;
        this.clanContributionService = clanContributionService;
    }

    @Override
    @Transactional
    public WorkoutDTO save(WorkoutDTO workoutDTO, UUID userId, User user) {
        Workout workout = new Workout();
        User client;
        if(!user.getUuid().equals(userId)){
            client = userRepository.findByUuid(userId).orElseThrow(() -> new EntityNotFoundException("No client"));
            Coach coach = this.coachRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException("No coach"));
            ClientCoach clientCoach = clientCoachRepository.findByClientAndCoach(client.getId(), user.getId()).orElseThrow(() -> new EntityNotFoundException("No role like this"));
            if(clientCoach.getStatus() == CoachStatus.ACTIVE){
                workout.setCoach(coach);
                workout.setUser(client);
            }else{
                throw new AccessDeniedException("Coach-client relationship is not active");
            }
        }else{
            User managedUser = userRepository.findByUuid(user.getUuid())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            workout.setUser(managedUser);
        }
        workout.setStatus(WorkoutStatus.DRAFT);
        workout.setDate(LocalDate.now());
        workout.setWorkoutTemplate(new WorkoutTemplate(workoutDTO.getWorkoutName()));

        return modelMapper.map(workoutRepository.save(workout), WorkoutDTO.class);
    }


    @Override
    public Map<String,List<WorkoutDTO>> findByUserId(UUID userUuid) {
        int userId = this.userRepository.findByUuid(userUuid).map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("Theres no user like this!"));

        List<WorkoutDTO> dtos = workoutRepository.findByUserId(userId)
                .stream()
                .map(workout -> {
                    WorkoutDTO workoutDTO = modelMapper.map(workout, WorkoutDTO.class);

                    Double sumOfKg = workoutRepository.getTonnageForWorkout(workout.getId());
                    workoutDTO.setTotal_tonnage_kg(sumOfKg != null ? sumOfKg : 0.0);

                    Double workoutScore = workoutDetailsRepository.getWorkoutScore(workout.getId());
                    Double workoutVolume = workoutDetailsRepository.getWorkoutVolume(workout.getId());

                    workoutDTO.setWorkoutScore(workoutScore != null ? workoutScore : 0.0);
                    workoutDTO.setWorkoutVolume(workoutVolume != null ? workoutVolume : 0.0);

                    workoutDTO.setWorkoutName(workout.getWorkoutTemplate().getWorkout_name());

                    return workoutDTO;
                })
                .collect(Collectors.toList());
        Map<String, List<WorkoutDTO>> result = new HashMap<>();
        for(WorkoutDTO workoutDTO : dtos){
            if(!result.containsKey(workoutDTO.getWorkoutName())){
                result.put(workoutDTO.getWorkoutName(), new ArrayList<>());
            }
                List<WorkoutDTO> updateList = result.get(workoutDTO.getWorkoutName());
                updateList.add(workoutDTO);
                result.put(workoutDTO.getWorkoutName(), updateList);

        }
        return result;
    }

    @Override
    @Transactional
    public void deleteWorkoutById(UUID workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new EntityNotFoundException("Workout not found"));

        workoutRepository.delete(workout);
    }

    @Override
    @Transactional
    public List<WorkoutDetailsDTO> findWorkoutDetailsById(UUID workoutId, User user) {
        Workout workout = this.workoutRepository.findById(workoutId).orElseThrow(() -> new EntityNotFoundException("Workout not found"));
        Optional<Coach> coachOpt = this.coachRepository.findByUser(user);

        if (!hasAccess(workout, coachOpt, user)) {
            throw new AccessDeniedException("Coach-client relationship is not active");
        }
        return workoutDetailsRepository.findByWorkoutId(workoutId).stream()
                .map(this::mapWorkoutDetailsToDTO)
                .toList();
    }

    private boolean hasAccess(Workout workout, Optional<Coach> coachOpt, User user) {
        boolean isOwner = workout.getUser().getId() == user.getId();

        boolean isAuthorizedCoach = coachOpt.isPresent()
                && workout.getCoach() != null
                && coachOpt.get().getId() == workout.getCoach().getId();

        return isOwner || isAuthorizedCoach;
    }

    @Override
    @Transactional
    public WorkoutDetailsDTO saveWorkoutDetails(WorkoutDetails workoutDetails) {

        if (workoutDetails.getExercise() == null || workoutDetails.getExercise().getId() == 0) {
            throw new IllegalArgumentException("Exercise id is required");
        }

        Exercise exercise = exerciseRepository.findById(workoutDetails.getExercise().getId())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        Optional<WorkoutDetails> existingDetail =
                workoutDetailsRepository.findExistingWorkoutDetail(
                        workoutDetails.getWorkoutId(),
                        exercise.getId()
                );

        if (existingDetail.isPresent()) {
            return mapWorkoutDetailsToDTO(existingDetail.get());
        }

        workoutDetails.setExercise(exercise);
        workoutDetails.setMuscleGroup(exercise.getMuscleGroup());
        workoutDetails.setVolume(0.0);
        workoutDetails.setEstimatedOneRepMax(0.0);
        workoutDetails.setExercisePoints(0.0);

        WorkoutDetails saved = workoutDetailsRepository.save(workoutDetails);

        return mapWorkoutDetailsToDTO(saved);
    }

    @Override
    public void deleteWorkoutDetailsById(int workoutDetailId) {
        workoutDetailsRepository.deleteById(workoutDetailId);
    }

    private WorkoutDetailsDTO mapWorkoutDetailsToDTO(WorkoutDetails workoutDetails) {
        WorkoutDetailsDTO dto = new WorkoutDetailsDTO();

        dto.setId(workoutDetails.getId());
        dto.setVolume(workoutDetails.getVolume());
        dto.setEstimatedOneRepMax(workoutDetails.getEstimatedOneRepMax());
        dto.setExercisePoints(workoutDetails.getExercisePoints());
        dto.setSuspicious(workoutDetails.isSuspicious());
        dto.setAnomalyScore(workoutDetails.getAnomalyScore());
        dto.setAnomalyReason(workoutDetails.getAnomalyReason());

        if (workoutDetails.getExercise() != null) {
            dto.setExercise(modelMapper.map(workoutDetails.getExercise(), ExerciseDTO.class));
        }

        return dto;
    }

    @Override
    @Transactional
    public WorkoutDTO finishWorkout(UUID workoutId, User user) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new EntityNotFoundException("Workout not found"));

        if (workout.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("You do not have access to this workout");
        }

        if (workout.getStatus() == WorkoutStatus.FINISHED) {
            return mapWorkoutToDTO(workout);
        }

            Double workoutScore = workoutDetailsRepository.getWorkoutScore(workout.getId());

            if (workoutScore == null || workoutScore <= 0) {
                throw new IllegalArgumentException("Workout cannot be finished without score");
            }
            this.clanContributionService.addContributionForFinishedWorkout(workout);
            workout.setStatus(WorkoutStatus.FINISHED);

            Workout saved = workoutRepository.save(workout);

            return mapWorkoutToDTO(saved);
    }

    private WorkoutDTO mapWorkoutToDTO(Workout workout) {
        WorkoutDTO workoutDTO = modelMapper.map(workout, WorkoutDTO.class);

        if (workout.getWorkoutTemplate() != null) {
            workoutDTO.setWorkoutName(workout.getWorkoutTemplate().getWorkout_name());
        }

        Double workoutScore = workoutDetailsRepository.getWorkoutScore(workout.getId());
        Double workoutVolume = workoutDetailsRepository.getWorkoutVolume(workout.getId());

        workoutDTO.setWorkoutScore(workoutScore != null ? workoutScore : 0.0);
        workoutDTO.setWorkoutVolume(workoutVolume != null ? workoutVolume : 0.0);

        Double sumOfKg = workoutRepository.getTonnageForWorkout(workout.getId());
        workoutDTO.setTotal_tonnage_kg(sumOfKg != null ? sumOfKg : 0.0);

        if (workout.getStatus() != null) {
            workoutDTO.setStatus(workout.getStatus().name());
        }

        List<WorkoutDetailsDTO> details = workoutDetailsRepository.findByWorkoutId(workout.getId())
                .stream()
                .map(this::mapWorkoutDetailsToDTO)
                .toList();

        workoutDTO.setWorkoutDetails(details);

        return workoutDTO;
    }

}