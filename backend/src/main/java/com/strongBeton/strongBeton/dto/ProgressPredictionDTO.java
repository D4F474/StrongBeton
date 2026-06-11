package com.strongBeton.strongBeton.dto;

public class ProgressPredictionDTO {

    private int exerciseId;
    private String exerciseName;

    private Double currentEstimatedOneRepMax;
    private Double predictedOneRepMaxAfter30Days;

    private String trend;

    private Double beta0;
    private Double beta1;

    private boolean reliable;
    private int dataPoints;

    public ProgressPredictionDTO() {
    }

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

    public Double getCurrentEstimatedOneRepMax() {
        return currentEstimatedOneRepMax;
    }

    public void setCurrentEstimatedOneRepMax(Double currentEstimatedOneRepMax) {
        this.currentEstimatedOneRepMax = currentEstimatedOneRepMax;
    }

    public Double getPredictedOneRepMaxAfter30Days() {
        return predictedOneRepMaxAfter30Days;
    }

    public void setPredictedOneRepMaxAfter30Days(Double predictedOneRepMaxAfter30Days) {
        this.predictedOneRepMaxAfter30Days = predictedOneRepMaxAfter30Days;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public Double getBeta0() {
        return beta0;
    }

    public void setBeta0(Double beta0) {
        this.beta0 = beta0;
    }

    public Double getBeta1() {
        return beta1;
    }

    public void setBeta1(Double beta1) {
        this.beta1 = beta1;
    }

    public boolean isReliable() {
        return reliable;
    }

    public void setReliable(boolean reliable) {
        this.reliable = reliable;
    }

    public int getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(int dataPoints) {
        this.dataPoints = dataPoints;
    }
}