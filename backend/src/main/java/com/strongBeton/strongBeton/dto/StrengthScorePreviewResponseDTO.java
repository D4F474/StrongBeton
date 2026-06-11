package com.strongBeton.strongBeton.dto;

public class StrengthScorePreviewResponseDTO {

    private int exerciseId;
    private String exerciseName;
    private String exerciseDifficulty;
    private double difficultyCoefficient;

    private double estimatedOneRepMax;
    private boolean oneRepMaxCalculated;
    private double volume;
    private double allometricStrength;
    private double exercisePoints;

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseDifficulty() {
        return exerciseDifficulty;
    }

    public void setExerciseDifficulty(String exerciseDifficulty) {
        this.exerciseDifficulty = exerciseDifficulty;
    }

    public double getDifficultyCoefficient() {
        return difficultyCoefficient;
    }

    public void setDifficultyCoefficient(double difficultyCoefficient) {
        this.difficultyCoefficient = difficultyCoefficient;
    }

    public double getEstimatedOneRepMax() {
        return estimatedOneRepMax;
    }

    public void setEstimatedOneRepMax(double estimatedOneRepMax) {
        this.estimatedOneRepMax = estimatedOneRepMax;
    }

    public boolean isOneRepMaxCalculated() {
        return oneRepMaxCalculated;
    }

    public void setOneRepMaxCalculated(boolean oneRepMaxCalculated) {
        this.oneRepMaxCalculated = oneRepMaxCalculated;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAllometricStrength() {
        return allometricStrength;
    }

    public void setAllometricStrength(double allometricStrength) {
        this.allometricStrength = allometricStrength;
    }

    public double getExercisePoints() {
        return exercisePoints;
    }

    public void setExercisePoints(double exercisePoints) {
        this.exercisePoints = exercisePoints;
    }
}