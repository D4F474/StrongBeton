package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.workout.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
    Optional<Workout> findByDate(LocalDate date);

    List<Workout> findByUserId(int userId);
    @Query(value = "SELECT total_tonnage_kg FROM workout_with_tonnage WHERE id_workout = :workoutId", nativeQuery = true)
    Double getTonnageForWorkout(@Param("workoutId") UUID workoutId);

    @Query(value = """
        SELECT COUNT(*)
        FROM workout
        WHERE user_id = :userId
        """, nativeQuery = true)
    int countWorkoutsByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT COUNT(*)
        FROM workout
        WHERE user_id = :userId
        AND MONTH(date) = MONTH(CURRENT_DATE())
        AND YEAR(date) = YEAR(CURRENT_DATE())
        """, nativeQuery = true)
    int countWorkoutsThisMonthByUserId(@Param("userId") int userId);
}
