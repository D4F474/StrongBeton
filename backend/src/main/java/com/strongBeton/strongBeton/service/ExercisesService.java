package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.ExerciseDTO;
import com.strongBeton.strongBeton.entity.Exercise;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface ExercisesService {
    Set<ExerciseDTO> findAllExercises();

    Set<String> findAllMuscleGroups();

    ExerciseDTO findById(int theId);

    ExerciseDTO save(Exercise exercise);

    void deleteById(int theId);
}
