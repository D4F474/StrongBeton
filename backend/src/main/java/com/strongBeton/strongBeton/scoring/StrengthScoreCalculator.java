package com.strongBeton.strongBeton.scoring;

import com.strongBeton.strongBeton.enums.ExerciseDifficulty;
import org.springframework.stereotype.Component;

import java.util.OptionalDouble;

@Component
public class StrengthScoreCalculator {

    public OptionalDouble estimateOneRepMax(double weight, int reps) {
        if (weight <= 0 || reps <= 0) {
            return OptionalDouble.empty();
        }

        if (reps == 1) {
            return OptionalDouble.of(weight);
        }

        if (reps >= 2 && reps <= 5) {
            return OptionalDouble.of(weight * Math.pow(reps, 0.10)); // Lombardi
        }

        if (reps >= 6 && reps <= 10) {
            return OptionalDouble.of(weight / (1.0278 - 0.0278 * reps)); // Brzycki
        }

        if (reps >= 11 && reps <= 20) {
            return OptionalDouble.of((100 * weight) / (52.2 + 41.9 * Math.exp(-0.055 * reps))); // Mayhew
        }

        return OptionalDouble.empty();
    }

    public double calculateSetVolume(double weight, int reps) {
        if (weight <= 0 || reps <= 0) {
            return 0;
        }

        return weight * reps;
    }

    public double calculateAllometricStrength(double oneRepMax, double bodyWeight) {
        if (oneRepMax <= 0 || bodyWeight <= 0) {
            return 0;
        }

        return oneRepMax / Math.pow(bodyWeight, 0.67);
    }

    public double calculateExercisePoints(
            double estimatedOneRepMax,
            double bodyWeight,
            double volume,
            ExerciseDifficulty difficulty
    ) {
        if (estimatedOneRepMax <= 0 || bodyWeight <= 0 || volume <= 0 || difficulty == null) {
            return 0;
        }

        return calculateAllometricStrength(estimatedOneRepMax, bodyWeight)
                * Math.log(volume + 1)
                * difficulty.getCoefficient();
    }

    public double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}