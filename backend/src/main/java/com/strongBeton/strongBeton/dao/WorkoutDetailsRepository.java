package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.WorkoutDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface WorkoutDetailsRepository extends JpaRepository<WorkoutDetails, Integer> {
    WorkoutDetails findExerciseById(int theId);

    @Query(value = "SELECT * FROM workout_details WHERE workout_uuid =?1", nativeQuery = true)
    List<WorkoutDetails> findByWorkoutId(UUID workoutId);

    void deleteByExerciseId(int theId);
}
