package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.ExerciseDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.dao.ExerciseRepository;
import com.strongBeton.strongBeton.dao.MuscleGroupRepository;
import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.entity.Exercise;
import com.strongBeton.strongBeton.entity.MuscleGroup;
import com.strongBeton.strongBeton.entity.WorkoutDetails;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkoutDetailsServiceImpl implements WorkoutDetailsService {

    private final WorkoutDetailsRepository workoutDetailsRepository;
    private final ExerciseRepository exerciseRepository;
    private final MuscleGroupRepository muscleGroupRepository;
    private final ModelMapper modelMapper;

    public WorkoutDetailsServiceImpl(WorkoutDetailsRepository workoutDetailsRepository,
                                     ExerciseRepository exerciseRepository,
                                     MuscleGroupRepository muscleGroupRepository,
                                     ModelMapper modelMapper){
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.exerciseRepository = exerciseRepository;
        this.muscleGroupRepository = muscleGroupRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public List<WorkoutDetailsDTO> findWorkoutDetailsById(UUID workoutId){
        return workoutDetailsRepository.findByWorkoutId(workoutId)
                .stream()
                .map(workout -> {
                    WorkoutDetailsDTO workoutDetailsDTO = modelMapper.map(workout, WorkoutDetailsDTO.class);
                    workoutDetailsDTO.setExercise(modelMapper.map(workout.getExercise(), ExerciseDTO.class));
                    workoutDetailsDTO.setMuscleGroup(workout.getMuscleGroup().getMuscleGroupName());
                    return workoutDetailsDTO;
                })
                .toList();
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
        System.out.println(workoutDetails);
        return modelMapper.map(workoutDetailsRepository.save(workoutDetails), WorkoutDetailsDTO.class);
    }

    @Override
    public void deleteWorkoutDetailsById(int workoutDetailId) {
        workoutDetailsRepository.deleteById(workoutDetailId);
    }
}
