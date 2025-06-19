package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout,Integer> {
    Optional<Workout> findByDate(LocalDate date);
    Optional<Workout> findByWorkoutName(String name);

    List<Workout> findByUserId(int userId);
    @Query(value = "SELECT total_tonnage_kg FROM workout_with_tonnage WHERE id_workout = :workoutId", nativeQuery = true)
    Double getTonnageForWorkout(@Param("workoutId") int workoutId);

    @Procedure("search_workouts_by_user")
    List<Workout> searchWorkoutByUser( int userId,String keyword);


}
