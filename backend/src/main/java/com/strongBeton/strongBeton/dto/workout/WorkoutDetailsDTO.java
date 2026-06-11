package com.strongBeton.strongBeton.dto.workout;

public class WorkoutDetailsDTO {

    private int id;
    private ExerciseDTO exercise;

    private Double volume;
    private Double estimatedOneRepMax;
    private Double exercisePoints;

    private boolean suspicious;
    private Double anomalyScore;
    private String anomalyReason;

    public WorkoutDetailsDTO() {
    }

    public WorkoutDetailsDTO(ExerciseDTO exercise, Double volume, Double estimatedOneRepMax,
                             Double exercisePoints, boolean suspicious, Double anomalyScore, String anomalyReason) {
        this.exercise = exercise;
        this.volume = volume;
        this.estimatedOneRepMax = estimatedOneRepMax;
        this.exercisePoints = exercisePoints;
        this.suspicious = suspicious;
        this.anomalyScore = anomalyScore;
        this.anomalyReason = anomalyReason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExerciseDTO getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDTO exercise) {
        this.exercise = exercise;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getEstimatedOneRepMax() {
        return estimatedOneRepMax;
    }

    public void setEstimatedOneRepMax(Double estimatedOneRepMax) {
        this.estimatedOneRepMax = estimatedOneRepMax;
    }

    public Double getExercisePoints() {
        return exercisePoints;
    }

    public void setExercisePoints(Double exercisePoints) {
        this.exercisePoints = exercisePoints;
    }

    public boolean isSuspicious() {
        return suspicious;
    }

    public void setSuspicious(boolean suspicious) {
        this.suspicious = suspicious;
    }

    public Double getAnomalyScore() {
        return anomalyScore;
    }

    public void setAnomalyScore(Double anomalyScore) {
        this.anomalyScore = anomalyScore;
    }

    public String getAnomalyReason() {
        return anomalyReason;
    }

    public void setAnomalyReason(String anomalyReason) {
        this.anomalyReason = anomalyReason;
    }
}
