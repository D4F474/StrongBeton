package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.SetsDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.*;

import java.util.List;
import java.util.Map;

public interface WorkoutService {
    WorkoutDTO save(WorkoutDTO workoutDTO, int userId);
    Map<String,List<WorkoutDTO>> findByUserId(int userId);
    List<WorkoutDTO> findBySearchbar(int userId, String keyword);

    void deleteWorkoutById(int theId);



}
