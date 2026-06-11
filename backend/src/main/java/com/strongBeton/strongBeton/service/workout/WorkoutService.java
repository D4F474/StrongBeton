package com.strongBeton.strongBeton.service.workout;

import com.strongBeton.strongBeton.dto.workout.ActiveWorkoutPreviewDTO;
import com.strongBeton.strongBeton.dto.workout.WorkoutDTO;
import com.strongBeton.strongBeton.dto.workout.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.entity.workout.WorkoutDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutService {
    WorkoutDTO save(WorkoutDTO workoutDTO, UUID userId, User user) ;
    Map<String,List<WorkoutDTO>> findByUserId(UUID userId);
    void deleteWorkoutById(UUID theId);
    List<WorkoutDetailsDTO> findWorkoutDetailsById(UUID workoutId, User user);
    WorkoutDetailsDTO saveWorkoutDetails(WorkoutDetails workoutDetails);
    void deleteWorkoutDetailsById(int workoutDetailId);
    WorkoutDTO finishWorkout(UUID workoutId, User user);
    Optional<ActiveWorkoutPreviewDTO> findActiveWorkoutPreview(User user);


}
