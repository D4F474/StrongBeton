package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.SetsDTO;
import com.strongBeton.strongBeton.entity.Sets;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetsService {
    List<SetsDTO> findSetsByWorkoutId(int workoutId);

    SetsDTO saveSet(Sets sets);

    void deleteSet(int theId);

}
