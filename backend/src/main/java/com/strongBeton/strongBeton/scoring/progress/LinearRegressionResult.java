package com.strongBeton.strongBeton.scoring.progress;

public record LinearRegressionResult(
        double beta0,
        double beta1,
        boolean reliable
) {
    public double predict(double x) {
        return beta0 + beta1 * x;
    }
}