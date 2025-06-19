package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.ExerciseDTO;
import com.strongBeton.strongBeton.dao.ExerciseRepository;
import com.strongBeton.strongBeton.dao.MuscleGroupRepository;
import com.strongBeton.strongBeton.entity.Exercise;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExercisesServiceImpl implements ExercisesService {

    ExerciseRepository exerciseRepository;
    MuscleGroupRepository muscleGroupRepository;
    ModelMapper modelMapper;
    
    @Autowired
    public ExercisesServiceImpl(ExerciseRepository exerciseRepository,
                                ModelMapper modelMapper,
                                MuscleGroupRepository muscleGroupRepository
                                ) {
        this.exerciseRepository = exerciseRepository;
        this.muscleGroupRepository = muscleGroupRepository;
        this.modelMapper = modelMapper;

    }

    @Override
    public Set<ExerciseDTO> findAllExercises() {
        return exerciseRepository.findAll().stream()
                .map(exercise -> modelMapper.map(exercise, ExerciseDTO.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> findAllMuscleGroups() {
        return muscleGroupRepository.findAll().stream()
                .map(muscleGroup -> muscleGroup.getMuscleGroupName())
                .collect(Collectors.toSet());
    }

    @Override
    public ExerciseDTO findById(int theId) {
        return modelMapper.map(exerciseRepository.findById(theId), ExerciseDTO.class);
    }

    @Override
    public ExerciseDTO save(Exercise exercise) {
        return modelMapper.map(exerciseRepository.save(exercise), ExerciseDTO.class);
    }

    @Override
    public void deleteById(int theId) {
        exerciseRepository.deleteById(theId);
    }
}
