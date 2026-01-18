package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.SetsDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WorkoutService {
    WorkoutDTO save(WorkoutDTO workoutDTO, UUID userId);
    Map<String,List<WorkoutDTO>> findByUserId(UUID userId);
    List<WorkoutDTO> findBySearchbar(UUID userId, String keyword);

    void deleteWorkoutById(UUID theId);



}
