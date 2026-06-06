package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dao.ExerciseRepository;
import com.strongBeton.strongBeton.dto.StrengthScorePreviewRequestDTO;
import com.strongBeton.strongBeton.dto.StrengthScorePreviewResponseDTO;
import com.strongBeton.strongBeton.entity.workout.Exercise;
import com.strongBeton.strongBeton.enums.ExerciseDifficulty;
import com.strongBeton.strongBeton.scoring.StrengthScoreCalculator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.OptionalDouble;

@Service
public class ScorePreviewImpl implements ScorePreviewService{

    private final ExerciseRepository exerciseRepository;
    private final StrengthScoreCalculator strengthScoreCalculator;

    @Autowired
    public ScorePreviewImpl(
            ExerciseRepository exerciseRepository,
            StrengthScoreCalculator strengthScoreCalculator
    ) {
        this.exerciseRepository = exerciseRepository;
        this.strengthScoreCalculator = strengthScoreCalculator;
    }

    public StrengthScorePreviewResponseDTO preview(StrengthScorePreviewRequestDTO request) {
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        ExerciseDifficulty difficulty = exercise.getExerciseDifficulty();
        // Ако при теб getter-ът е друг, примерно:
        // ExerciseDifficulty difficulty = exercise.getDifficultyType();

        OptionalDouble oneRmOptional = strengthScoreCalculator.estimateOneRepMax(
                request.getWeight(),
                request.getReps()
        );

        double oneRm = oneRmOptional.orElse(0);
        double volume = strengthScoreCalculator.calculateSetVolume(
                request.getWeight(),
                request.getReps()
        );

        double allometricStrength = strengthScoreCalculator.calculateAllometricStrength(
                oneRm,
                request.getBodyWeight()
        );

        double points = strengthScoreCalculator.calculateExercisePoints(
                oneRm,
                request.getBodyWeight(),
                volume,
                difficulty
        );

        StrengthScorePreviewResponseDTO response = new StrengthScorePreviewResponseDTO();

        response.setExerciseId(exercise.getId());
        response.setExerciseName(exercise.getName());
        response.setExerciseDifficulty(difficulty.name());
        response.setDifficultyCoefficient(difficulty.getCoefficient());

        response.setOneRepMaxCalculated(oneRmOptional.isPresent());
        response.setEstimatedOneRepMax(strengthScoreCalculator.round(oneRm, 2));
        response.setVolume(strengthScoreCalculator.round(volume, 2));
        response.setAllometricStrength(strengthScoreCalculator.round(allometricStrength, 2));
        response.setExercisePoints(strengthScoreCalculator.round(points, 2));

        return response;
    }
}
