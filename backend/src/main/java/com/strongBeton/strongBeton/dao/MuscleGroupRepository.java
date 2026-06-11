package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.workout.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MuscleGroupRepository extends JpaRepository<MuscleGroup,Integer> {

    @Query(value = "SELECT * FROM muscle_group WHERE muscle_group_name=:muscleGroupName", nativeQuery = true)
    Optional<MuscleGroup> findByMuscleGroupName(@Param("muscleGroupName") String muscleGroupName);
}
