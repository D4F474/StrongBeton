package com.strongBeton.strongBeton.service.workout;

import com.strongBeton.strongBeton.dto.workout.SetsDTO;
import com.strongBeton.strongBeton.entity.workout.Sets;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetsService {
    public List<SetsDTO> findSetsByWorkoutId(int workoutDetailsId);

    public SetsDTO saveSet(Sets sets);

    public void deleteSet(int theId);

}
