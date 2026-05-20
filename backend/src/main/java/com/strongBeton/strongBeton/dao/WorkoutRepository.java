package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
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
}
