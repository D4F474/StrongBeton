package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.*;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.entity.*;
import com.strongBeton.strongBeton.enums.CoachStatus;
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

    public WorkoutServiceImpl(ModelMapper modelMapper,
                              WorkoutDetailsRepository workoutDetailsRepository,
                              WorkoutRepository workoutRepository,
                              SetsRepository setsRepository,
                              UserRepository userRepository,
                              ExerciseRepository exerciseRepository,
                              MuscleGroupRepository muscleGroupRepository,
                              CoachRepository coachRepository,
                              ClientCoachRepository clientCoachRepository) {
        this.modelMapper = modelMapper;
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.workoutRepository = workoutRepository;
        this.setsRepository = setsRepository;
        this.userRepository = userRepository;
        this.muscleGroupRepository = muscleGroupRepository;
        this.exerciseRepository = exerciseRepository;
        this.coachRepository = coachRepository;
        this.clientCoachRepository = clientCoachRepository;
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
                .map(workout ->{
                    WorkoutDTO workoutDTO = modelMapper.map(workout, WorkoutDTO.class);
                    Double sumOfKg = workoutRepository.getTonnageForWorkout(workout.getId());
                    workoutDTO.setTotal_tonnage_kg(sumOfKg != null ? sumOfKg : 0.0 );
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
                    .map(workoutDetails -> {
                        WorkoutDetailsDTO workoutDetailsDTO = modelMapper.map(workoutDetails, WorkoutDetailsDTO.class);
                        workoutDetailsDTO.setExercise(modelMapper.map(workoutDetails.getExercise(), ExerciseDTO.class));
                        workoutDetailsDTO.setMuscleGroup(workoutDetails.getMuscleGroup().getMuscleGroupName());
                        return workoutDetailsDTO;
                    })
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

        if (workoutDetails.getExercise() != null) {
            Optional<Exercise> existingExercise = exerciseRepository.findByName(workoutDetails.getExercise().getName());
            if (existingExercise.isPresent()) {
                workoutDetails.setExercise(existingExercise.get());
            } else {
                exerciseRepository.save(workoutDetails.getExercise());
            }
        }

        if (workoutDetails.getMuscleGroup() != null) {
            Optional<MuscleGroup> existingMuscleGroup = muscleGroupRepository.findByMuscleGroupName(workoutDetails.getMuscleGroup().getMuscleGroupName());
            if (existingMuscleGroup.isPresent()) {
                workoutDetails.setMuscleGroup(existingMuscleGroup.get());
            } else {
                muscleGroupRepository.save(workoutDetails.getMuscleGroup());
            }
        }
        return modelMapper.map(workoutDetailsRepository.save(workoutDetails), WorkoutDetailsDTO.class);
    }

    @Override
    public void deleteWorkoutDetailsById(int workoutDetailId) {
        workoutDetailsRepository.deleteById(workoutDetailId);
    }
}