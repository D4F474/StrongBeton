package com.strongBeton.strongBeton.scoring.anomaly;

public record AnomalyResult(
        boolean suspicious,
        Double anomalyScore,
        String anomalyReason
) {
    public static AnomalyResult normal() {
        return new AnomalyResult(false, 0.0, null);
    }
}
