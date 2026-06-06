package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dto.ProgressPredictionDTO;

import java.util.UUID;

public interface ProgressService {
    public ProgressPredictionDTO predictExerciseProgress(UUID userUuid, int exerciseId);
}
