package com.strongBeton.strongBeton.scoring.progress;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProgressPredictor {

    public LinearRegressionResult predict(List<ProgressPoint> points) {
        if (points == null || points.size() < 3) {
            return new LinearRegressionResult(0, 0, false);
        }

        int n = points.size();

        double sumX = points.stream()
                .mapToDouble(ProgressPoint::x)
                .sum();

        double sumY = points.stream()
                .mapToDouble(ProgressPoint::y)
                .sum();

        double sumXY = points.stream()
                .mapToDouble(point -> point.x() * point.y())
                .sum();

        double sumX2 = points.stream()
                .mapToDouble(point -> point.x() * point.x())
                .sum();

        double denominator = n * sumX2 - sumX * sumX;

        if (denominator == 0) {
            return new LinearRegressionResult(0, 0, false);
        }

        double beta1 = (n * sumXY - sumX * sumY) / denominator;
        double beta0 = (sumY - beta1 * sumX) / n;

        return new LinearRegressionResult(beta0, beta1, true);
    }

    public String classifyTrend(double beta1) {
        if (beta1 > 0.05) {
            return "UP";
        }

        if (beta1 < -0.05) {
            return "DOWN";
        }

        return "STABLE";
    }

    public double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
