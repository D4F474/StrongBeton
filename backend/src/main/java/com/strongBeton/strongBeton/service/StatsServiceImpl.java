package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dao.KGLogRepository;
import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.dao.WorkoutRepository;
import com.strongBeton.strongBeton.dao.projection.DailyVolumeRow;
import com.strongBeton.strongBeton.dao.projection.RecentRecordRow;
import com.strongBeton.strongBeton.dto.stats.RecentRecordDTO;
import com.strongBeton.strongBeton.dto.stats.StatsOverviewDTO;
import com.strongBeton.strongBeton.dto.stats.WeightHistoryDTO;
import com.strongBeton.strongBeton.dto.stats.WeeklyVolumeDTO;
import com.strongBeton.strongBeton.entity.user.KGLogs;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.entity.workout.Workout;
import com.strongBeton.strongBeton.enums.WorkoutStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {

    private static final double WEEKLY_VOLUME_TARGET = 50_000.0;
    private static final int TARGET_WORKOUTS_PER_WEEK = 4;

    private final WorkoutRepository workoutRepository;
    private final WorkoutDetailsRepository workoutDetailsRepository;
    private final KGLogRepository kgLogRepository;

    public StatsServiceImpl(
            WorkoutRepository workoutRepository,
            WorkoutDetailsRepository workoutDetailsRepository,
            KGLogRepository kgLogRepository
    ) {
        this.workoutRepository = workoutRepository;
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.kgLogRepository = kgLogRepository;
    }

    @Override
    public StatsOverviewDTO getStatsOverview(User currentUser) {
        int userId = currentUser.getId();

        String finishedStatus = WorkoutStatus.FINISHED.name();
        WorkoutStatus finishedEnum = WorkoutStatus.FINISHED;

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        double weeklyVolume = safeDouble(
                workoutDetailsRepository.getVolumeBetween(
                        userId,
                        finishedStatus,
                        startOfWeek,
                        endOfWeek
                )
        );

        double weeklyScore = safeDouble(
                workoutDetailsRepository.getScoreBetween(
                        userId,
                        finishedStatus,
                        startOfWeek,
                        endOfWeek
                )
        );

        StatsOverviewDTO dto = new StatsOverviewDTO();

        dto.setStrengthScore((int) Math.round(weeklyScore));
        dto.setStrengthScoreDelta(
                calculateStrengthScoreDelta(userId, finishedStatus, startOfWeek, endOfWeek)
        );

        dto.setWeeklyVolume(weeklyVolume);
        dto.setWeeklyVolumeTarget(WEEKLY_VOLUME_TARGET);
        dto.setWeeklyVolumePercent(calculatePercent(weeklyVolume, WEEKLY_VOLUME_TARGET));

        dto.setTrainingStreak(calculateCurrentStreak(userId, finishedEnum));
        dto.setBestStreak(calculateBestStreak(userId, finishedEnum));

        dto.setPersonalRecords(
                workoutDetailsRepository.countTrackedPersonalRecords(userId, finishedStatus)
        );

        dto.setRecordsThisMonth(
                workoutDetailsRepository.countRecordsThisMonth(userId, finishedStatus)
        );

        dto.setConsistencyPercent(
                calculateWeeklyConsistency(userId, finishedStatus, startOfWeek, endOfWeek)
        );

        dto.setLoadQuality(
                calculateLoadQuality(userId, finishedStatus, startOfWeek, endOfWeek)
        );

        dto.setWeeklyVolumeBars(
                buildWeeklyVolumeBars(userId, finishedStatus, startOfWeek, endOfWeek)
        );

        dto.setRecentRecords(
                buildRecentRecords(userId, finishedStatus)
        );

        dto.setWeightHistory(
                buildWeightHistory(userId)
        );

        return dto;
    }

    private List<WeightHistoryDTO> buildWeightHistory(int userId) {
        List<KGLogs> logs = kgLogRepository.findByUserIdOrderByLoggedAtAsc(userId);

        if (logs == null || logs.isEmpty()) {
            return List.of();
        }

        return logs.stream()
                .map(log -> new WeightHistoryDTO(
                        log.getKg(),
                        log.getLoggedAt()
                ))
                .toList();
    }

    private int calculateStrengthScoreDelta(
            int userId,
            String status,
            LocalDate startOfWeek,
            LocalDate endOfWeek
    ) {
        double weeklyScore = safeDouble(
                workoutDetailsRepository.getScoreBetween(
                        userId,
                        status,
                        startOfWeek,
                        endOfWeek
                )
        );

        return (int) Math.round(weeklyScore * 0.10);
    }

    private List<WeeklyVolumeDTO> buildWeeklyVolumeBars(
            int userId,
            String status,
            LocalDate startOfWeek,
            LocalDate endOfWeek
    ) {
        List<DailyVolumeRow> rows = workoutDetailsRepository.findDailyVolumeBetween(
                userId,
                status,
                startOfWeek,
                endOfWeek
        );

        Map<LocalDate, Double> volumeByDate = rows == null
                ? Map.of()
                : rows.stream()
                .filter(row -> row.getWorkoutDate() != null)
                .collect(Collectors.toMap(
                        DailyVolumeRow::getWorkoutDate,
                        row -> safeDouble(row.getVolume()),
                        Double::sum
                ));

        List<WeeklyVolumeDTO> bars = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);

            String day = date.getDayOfWeek()
                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    .toUpperCase(Locale.ROOT);

            double volume = volumeByDate.getOrDefault(date, 0.0);

            bars.add(new WeeklyVolumeDTO(day, date, volume));
        }

        return bars;
    }

    private List<RecentRecordDTO> buildRecentRecords(int userId, String status) {
        List<RecentRecordRow> rows = workoutDetailsRepository.findRecentRecords(userId, status);

        if (rows == null || rows.isEmpty()) {
            return List.of();
        }

        return rows.stream()
                .map(row -> new RecentRecordDTO(
                        row.getExerciseName(),
                        row.getKg() == null ? 0f : row.getKg(),
                        row.getReps() == null ? 0 : row.getReps(),
                        row.getEstimatedOneRepMax() == null ? 0.0 : row.getEstimatedOneRepMax(),
                        row.getWorkoutDate()
                ))
                .toList();
    }

    private int calculateWeeklyConsistency(
            int userId,
            String status,
            LocalDate startOfWeek,
            LocalDate endOfWeek
    ) {
        int workouts = workoutRepository.countFinishedWorkoutsBetween(
                userId,
                status,
                startOfWeek,
                endOfWeek
        );

        int percent = (int) Math.round(
                (workouts / (double) TARGET_WORKOUTS_PER_WEEK) * 100
        );

        return Math.min(percent, 100);
    }

    private String calculateLoadQuality(
            int userId,
            String status,
            LocalDate startOfWeek,
            LocalDate endOfWeek
    ) {
        long totalDetails = workoutDetailsRepository.countDetailsBetween(
                userId,
                status,
                startOfWeek,
                endOfWeek
        );

        if (totalDetails <= 0) {
            return "N/A";
        }

        long suspiciousDetails = workoutDetailsRepository.countSuspiciousDetailsBetween(
                userId,
                status,
                startOfWeek,
                endOfWeek
        );

        double averageAnomaly = safeDouble(
                workoutDetailsRepository.getAverageAnomalyScoreBetween(
                        userId,
                        status,
                        startOfWeek,
                        endOfWeek
                )
        );

        double suspiciousRatio = suspiciousDetails / (double) totalDetails;

        if (suspiciousRatio == 0 && averageAnomaly < 0.15) {
            return "A+";
        }

        if (suspiciousRatio <= 0.05 && averageAnomaly < 0.25) {
            return "A";
        }

        if (suspiciousRatio <= 0.10 && averageAnomaly < 0.35) {
            return "A-";
        }

        if (suspiciousRatio <= 0.20 && averageAnomaly < 0.50) {
            return "B";
        }

        return "C";
    }

    private int calculateCurrentStreak(int userId, WorkoutStatus status) {
        List<LocalDate> dates = workoutRepository.findFinishedWorkoutDatesByUserId(
                userId,
                status
        );

        if (dates == null || dates.isEmpty()) {
            return 0;
        }

        Set<LocalDate> dateSet = new HashSet<>(dates);

        LocalDate cursor = LocalDate.now();

        if (!dateSet.contains(cursor)) {
            cursor = cursor.minusDays(1);
        }

        int streak = 0;

        while (dateSet.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }

        return streak;
    }

    private int calculateBestStreak(int userId, WorkoutStatus status) {
        List<LocalDate> dates = workoutRepository.findFinishedWorkoutDatesByUserId(
                userId,
                status
        );

        if (dates == null || dates.isEmpty()) {
            return 0;
        }

        List<LocalDate> sortedDates = dates.stream()
                .distinct()
                .sorted()
                .toList();

        int best = 1;
        int current = 1;

        for (int i = 1; i < sortedDates.size(); i++) {
            LocalDate previous = sortedDates.get(i - 1);
            LocalDate currentDate = sortedDates.get(i);

            if (currentDate.equals(previous.plusDays(1))) {
                current++;
            } else {
                current = 1;
            }

            best = Math.max(best, current);
        }

        return best;
    }

    private int calculatePercent(double value, double target) {
        if (target <= 0) {
            return 0;
        }

        int percent = (int) Math.round((value / target) * 100);

        return Math.min(percent, 100);
    }

    private double safeDouble(Double value) {
        return value == null ? 0.0 : value;
    }
}
