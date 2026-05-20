package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.SetsDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WorkoutService {
    WorkoutDTO save(WorkoutDTO workoutDTO, UUID userId, User user) ;
    Map<String,List<WorkoutDTO>> findByUserId(UUID userId);
    void deleteWorkoutById(UUID theId);
    List<WorkoutDetailsDTO> findWorkoutDetailsById(UUID workoutId, User user);
    WorkoutDetailsDTO saveWorkoutDetails(WorkoutDetails workoutDetails);
    void deleteWorkoutDetailsById(int workoutDetailId);



}
