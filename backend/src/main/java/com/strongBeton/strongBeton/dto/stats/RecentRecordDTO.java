package com.strongBeton.strongBeton.dto.stats;

import java.time.LocalDate;

public class RecentRecordDTO {

    private String exerciseName;
    private float kg;
    private int reps;
    private Double estimatedOneRepMax;
    private LocalDate date;

    public RecentRecordDTO() {
    }

    public RecentRecordDTO(String exerciseName, float kg, int reps, Double estimatedOneRepMax, LocalDate date) {
        this.exerciseName = exerciseName;
        this.kg = kg;
        this.reps = reps;
        this.estimatedOneRepMax = estimatedOneRepMax;
        this.date = date;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public float getKg() {
        return kg;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public Double getEstimatedOneRepMax() {
        return estimatedOneRepMax;
    }

    public void setEstimatedOneRepMax(Double estimatedOneRepMax) {
        this.estimatedOneRepMax = estimatedOneRepMax;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
