package com.strongBeton.strongBeton.dto.user;

import java.util.UUID;

public class UserStatsDTO {
    private UUID userId;
    private String username;

    private int totalWorkouts;
    private int totalWorkoutsThisMonth;

    private Double totalVolume;
    private Double totalVolumeThisMonth;

    private Double totalScore;
    private Double totalScoreThisMonth;

    private Double bestEstimatedOneRepMax;
    private String bestExerciseName;

    private String mostUsedExerciseName;
    private int mostUsedExerciseCount;

    private Double averageWorkoutScore;

    public UserStatsDTO() {
    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalWorkouts() {
        return totalWorkouts;
    }

    public void setTotalWorkouts(int totalWorkouts) {
        this.totalWorkouts = totalWorkouts;
    }

    public int getTotalWorkoutsThisMonth() {
        return totalWorkoutsThisMonth;
    }

    public void setTotalWorkoutsThisMonth(int totalWorkoutsThisMonth) {
        this.totalWorkoutsThisMonth = totalWorkoutsThisMonth;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Double getTotalVolumeThisMonth() {
        return totalVolumeThisMonth;
    }

    public void setTotalVolumeThisMonth(Double totalVolumeThisMonth) {
        this.totalVolumeThisMonth = totalVolumeThisMonth;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Double getTotalScoreThisMonth() {
        return totalScoreThisMonth;
    }

    public void setTotalScoreThisMonth(Double totalScoreThisMonth) {
        this.totalScoreThisMonth = totalScoreThisMonth;
    }

    public Double getBestEstimatedOneRepMax() {
        return bestEstimatedOneRepMax;
    }

    public void setBestEstimatedOneRepMax(Double bestEstimatedOneRepMax) {
        this.bestEstimatedOneRepMax = bestEstimatedOneRepMax;
    }

    public String getBestExerciseName() {
        return bestExerciseName;
    }

    public void setBestExerciseName(String bestExerciseName) {
        this.bestExerciseName = bestExerciseName;
    }

    public String getMostUsedExerciseName() {
        return mostUsedExerciseName;
    }

    public void setMostUsedExerciseName(String mostUsedExerciseName) {
        this.mostUsedExerciseName = mostUsedExerciseName;
    }

    public int getMostUsedExerciseCount() {
        return mostUsedExerciseCount;
    }

    public void setMostUsedExerciseCount(int mostUsedExerciseCount) {
        this.mostUsedExerciseCount = mostUsedExerciseCount;
    }

    public Double getAverageWorkoutScore() {
        return averageWorkoutScore;
    }

    public void setAverageWorkoutScore(Double averageWorkoutScore) {
        this.averageWorkoutScore = averageWorkoutScore;
    }
}
