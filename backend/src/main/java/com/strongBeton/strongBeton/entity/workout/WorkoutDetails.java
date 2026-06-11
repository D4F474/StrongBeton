package com.strongBeton.strongBeton.entity.workout;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="workout_details")
public class WorkoutDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="muscle_group_id")
    private MuscleGroup muscleGroup;

    @Column(name="workout_uuid")
    private UUID workoutId;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "estimated_1rm")
    private Double estimatedOneRepMax;

    @Column(name = "exercise_points")
    private Double exercisePoints;

    @Column(name = "is_suspicious")
    private boolean suspicious;

    @Column(name = "anomaly_score")
    private Double anomalyScore;

    @Column(name = "anomaly_reason")
    private String anomalyReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    public WorkoutDetails() {
    }

    public WorkoutDetails(Exercise exercise, MuscleGroup muscleGroup, UUID workoutId, Double volume,
                          Double estimatedOneRepMax, Double exercisePoints, boolean suspicious,
                          Double anomalyScore, String anomalyReason, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.exercise = exercise;
        this.muscleGroup = muscleGroup;
        this.workoutId = workoutId;
        this.volume = volume;
        this.estimatedOneRepMax = estimatedOneRepMax;
        this.exercisePoints = exercisePoints;
        this.suspicious = suspicious;
        this.anomalyScore = anomalyScore;
        this.anomalyReason = anomalyReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }


    public UUID getWorkoutId() {
        return workoutId;
    }

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public void setWorkoutId(UUID workoutId) {
        this.workoutId = workoutId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "WorkoutDetails{" +
                "id=" + id +
                ", exercise=" + exercise +
                ", muscleGroup=" + muscleGroup +
                ", workoutId=" + workoutId +
                '}';
    }
}
