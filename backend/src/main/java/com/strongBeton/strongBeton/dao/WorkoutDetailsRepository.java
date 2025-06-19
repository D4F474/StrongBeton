package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.WorkoutDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkoutDetailsRepository extends JpaRepository<WorkoutDetails, Integer> {
    WorkoutDetails findExerciseById(int theId);

    @Query(value = "SELECT * FROM workout_details WHERE workout_id =?1", nativeQuery = true)
    List<WorkoutDetails> findByWorkoutId(int workoutId);

    void deleteByExerciseId(int theId);
}
