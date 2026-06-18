package com.strongBeton.strongBeton.scoring.anomaly;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnomalyDetector {

    private static final double MAX_REASONABLE_DAILY_PROGRESS = 0.5;
    private static final double MAX_REASONABLE_PERCENT_JUMP = 0.30;

    public AnomalyResult detect(
            double currentOneRepMax,
            Double previousBestOneRepMax,
            Long daysSincePrevious,
            List<Double> previousValues
    ) {

        if (currentOneRepMax <= 0) {
            return AnomalyResult.normal();
        }

        if (previousBestOneRepMax == null || previousBestOneRepMax <= 0) {
            return AnomalyResult.normal();
        }

        double difference = currentOneRepMax - previousBestOneRepMax;
        double percentJump = difference / previousBestOneRepMax;

        if (percentJump > MAX_REASONABLE_PERCENT_JUMP) {
            return new AnomalyResult(
                    true,
                    round(percentJump, 4),
                    "Unusually large 1RM increase"
            );
        }

        if (daysSincePrevious != null && daysSincePrevious > 0) {
            double dailyProgress = difference / daysSincePrevious;

            if (dailyProgress > MAX_REASONABLE_DAILY_PROGRESS) {
                return new AnomalyResult(
                        true,
                        round(dailyProgress, 4),
                        "Unusually fast progress rate"
                );
            }
        }
        double zScore = calculateZScore(currentOneRepMax, previousValues);

        if (zScore >= 3.0) {
            return new AnomalyResult(
                    true,
                    round(zScore, 4),
                    "Statistical outlier based on previous performance"
            );
        }

        return AnomalyResult.normal();
    }

    public double calculateZScore(double currentValue, List<Double> previousValues) {
        if (previousValues == null || previousValues.size() < 5) {
            return 0.0;
        }

        double mean = previousValues.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double variance = previousValues.stream()
                .mapToDouble(value -> Math.pow(value - mean, 2))
                .average()
                .orElse(0.0);

        double standardDeviation = Math.sqrt(variance);

        if (standardDeviation == 0) {
            return 0.0;
        }

        return (currentValue - mean) / standardDeviation;
    }

    private double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}