package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.WorkoutDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface WorkoutDetailsService {

    List<WorkoutDetailsDTO> findWorkoutDetailsById(UUID workoutId);

    WorkoutDetailsDTO saveWorkoutDetails(WorkoutDetails workoutDetails);

    void deleteWorkoutDetailsById(int workoutDetailId);
}
