package com.strongBeton.strongBeton.dto.stats;

import java.util.List;

public class StatsOverviewDTO {
    private int strengthScore;
    private int strengthScoreDelta;

    private double weeklyVolume;
    private double weeklyVolumeTarget;
    private int weeklyVolumePercent;

    private int trainingStreak;
    private int bestStreak;

    private int personalRecords;
    private int recordsThisMonth;

    private int consistencyPercent;
    private String loadQuality;

    private List<WeeklyVolumeDTO> weeklyVolumeBars;
    private List<RecentRecordDTO> recentRecords;
    private List<WeightHistoryDTO> weightHistory;

    public StatsOverviewDTO() {
    }

    public int getStrengthScore() {
        return strengthScore;
    }

    public void setStrengthScore(int strengthScore) {
        this.strengthScore = strengthScore;
    }

    public int getStrengthScoreDelta() {
        return strengthScoreDelta;
    }

    public void setStrengthScoreDelta(int strengthScoreDelta) {
        this.strengthScoreDelta = strengthScoreDelta;
    }

    public double getWeeklyVolume() {
        return weeklyVolume;
    }

    public void setWeeklyVolume(double weeklyVolume) {
        this.weeklyVolume = weeklyVolume;
    }

    public double getWeeklyVolumeTarget() {
        return weeklyVolumeTarget;
    }

    public void setWeeklyVolumeTarget(double weeklyVolumeTarget) {
        this.weeklyVolumeTarget = weeklyVolumeTarget;
    }

    public int getWeeklyVolumePercent() {
        return weeklyVolumePercent;
    }

    public void setWeeklyVolumePercent(int weeklyVolumePercent) {
        this.weeklyVolumePercent = weeklyVolumePercent;
    }

    public int getTrainingStreak() {
        return trainingStreak;
    }

    public void setTrainingStreak(int trainingStreak) {
        this.trainingStreak = trainingStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }

    public int getPersonalRecords() {
        return personalRecords;
    }

    public void setPersonalRecords(int personalRecords) {
        this.personalRecords = personalRecords;
    }

    public int getRecordsThisMonth() {
        return recordsThisMonth;
    }

    public void setRecordsThisMonth(int recordsThisMonth) {
        this.recordsThisMonth = recordsThisMonth;
    }

    public int getConsistencyPercent() {
        return consistencyPercent;
    }

    public void setConsistencyPercent(int consistencyPercent) {
        this.consistencyPercent = consistencyPercent;
    }

    public String getLoadQuality() {
        return loadQuality;
    }

    public void setLoadQuality(String loadQuality) {
        this.loadQuality = loadQuality;
    }

    public List<WeeklyVolumeDTO> getWeeklyVolumeBars() {
        return weeklyVolumeBars;
    }

    public void setWeeklyVolumeBars(List<WeeklyVolumeDTO> weeklyVolumeBars) {
        this.weeklyVolumeBars = weeklyVolumeBars;
    }

    public List<RecentRecordDTO> getRecentRecords() {
        return recentRecords;
    }

    public void setRecentRecords(List<RecentRecordDTO> recentRecords) {
        this.recentRecords = recentRecords;
    }

    public List<WeightHistoryDTO> getWeightHistory() {
        return weightHistory;
    }

    public void setWeightHistory(List<WeightHistoryDTO> weightHistory) {
        this.weightHistory = weightHistory;
    }
}
