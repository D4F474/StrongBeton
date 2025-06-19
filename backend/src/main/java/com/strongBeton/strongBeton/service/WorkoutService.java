package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.SetsDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDTO;
import com.strongBeton.strongBeton.DTO.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.*;

import java.util.List;

public interface WorkoutService {

    User findUserByFromUserId(int theId);

    List<LeaderBoard>findLeaderBoardForUser();

    List<WorkoutDTO> findAll();
    WorkoutDTO findById(int theId);
    WorkoutDTO save(Workout workout);
    List<WorkoutDTO> findByUserId(int userId);
    List<WorkoutDTO> findBySearchbar(int userId, String keyword);

    List<WorkoutDetailsDTO> findWorkoutDetailsById(int workoutId);
    WorkoutDetailsDTO findWorkoutDetailById(int theId);
    WorkoutDetailsDTO saveWorkoutDetails(WorkoutDetails workoutDetails);

    WorkoutDTO addExerciseToWorkout(WorkoutDetails workoutDetails);
    List<SetsDTO> findSetsByWorkoutId(int workoutId);
    SetsDTO saveSet(Sets sets);

    void deleteWorkoutById(int theId);
    void deleteWorkoutDetailsExerciseById(int theId);
    void deleteSet(int theId);


    void deleteWorkoutDetailsById(int workoutDetailId);
}
