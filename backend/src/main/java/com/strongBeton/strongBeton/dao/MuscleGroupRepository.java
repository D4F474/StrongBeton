package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MuscleGroupRepository extends JpaRepository<MuscleGroup,Integer> {
    Optional<MuscleGroup> findByMuscleGroupName(String muscleGroupName);
}
