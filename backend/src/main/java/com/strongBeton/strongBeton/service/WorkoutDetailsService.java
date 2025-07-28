package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.WorkoutDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkoutDetailsService {

    List<WorkoutDetailsDTO> findWorkoutDetailsById(int workoutId);

    WorkoutDetailsDTO saveWorkoutDetails(WorkoutDetails workoutDetails);

    void deleteWorkoutDetailsById(int workoutDetailId);
}
