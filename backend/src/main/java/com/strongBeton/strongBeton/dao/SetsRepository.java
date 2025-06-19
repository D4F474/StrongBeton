package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.DTO.SetsDTO;
import com.strongBeton.strongBeton.entity.Sets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SetsRepository extends JpaRepository<Sets, Integer> {
    @Query(value = "SELECT * FROM Sets WHERE workout_details_id = ?1", nativeQuery = true)
    List<Sets> findSetsByWorkoutDetailsId(int workoutId);
}
