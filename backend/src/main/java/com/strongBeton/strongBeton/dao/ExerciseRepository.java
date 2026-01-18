package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

    Optional<Exercise> findByName(String name);
}
