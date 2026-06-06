package com.strongBeton.strongBeton.enums;

public enum ExerciseDifficulty {

    COMPOUND(1.5),
    SECONDARY(1.2),
    ISOLATION(0.8);

    private final double coefficient;

    ExerciseDifficulty(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return coefficient;
    }
}
