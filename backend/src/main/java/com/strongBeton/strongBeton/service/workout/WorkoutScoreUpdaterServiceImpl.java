package com.strongBeton.strongBeton.service.workout;

import com.strongBeton.strongBeton.dao.SetsRepository;
import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.dao.WorkoutRepository;
import com.strongBeton.strongBeton.entity.workout.Sets;
import com.strongBeton.strongBeton.entity.workout.Workout;
import com.strongBeton.strongBeton.entity.workout.WorkoutDetails;
import com.strongBeton.strongBeton.enums.ExerciseDifficulty;
import com.strongBeton.strongBeton.scoring.StrengthScoreCalculator;
import com.strongBeton.strongBeton.scoring.anomaly.AnomalyDetector;
import com.strongBeton.strongBeton.scoring.anomaly.AnomalyResult;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class WorkoutScoreUpdaterServiceImpl implements WorkoutScoreUpdaterService {
    private final WorkoutDetailsRepository workoutDetailsRepository;
    private final WorkoutRepository workoutRepository;
    private final SetsRepository setsRepository;
    private final StrengthScoreCalculator strengthScoreCalculator;
    private final AnomalyDetector anomalyDetector;

    public WorkoutScoreUpdaterServiceImpl(
            WorkoutDetailsRepository workoutDetailsRepository,
            WorkoutRepository workoutRepository,
            SetsRepository setsRepository,
            StrengthScoreCalculator strengthScoreCalculator,
            AnomalyDetector anomalyDetector
    ) {
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.workoutRepository = workoutRepository;
        this.setsRepository = setsRepository;
        this.strengthScoreCalculator = strengthScoreCalculator;
        this.anomalyDetector = anomalyDetector;
    }

    @Override
    @Transactional
    public void recalculateWorkoutDetailScore(int workoutDetailsId) {
        WorkoutDetails detail = workoutDetailsRepository.findById(workoutDetailsId)
                .orElseThrow(() -> new EntityNotFoundException("Workout detail not found"));

        Workout workout = workoutRepository.findById(detail.getWorkoutId())
                .orElseThrow(() -> new EntityNotFoundException("Workout not found"));

        List<Sets> sets = setsRepository.findSetsByWorkoutDetailsId(workoutDetailsId);

        if (sets == null || sets.isEmpty()) {
            detail.setVolume(0.0);
            detail.setEstimatedOneRepMax(0.0);
            detail.setExercisePoints(0.0);
            workoutDetailsRepository.save(detail);
            return;
        }

        double totalVolume = sets.stream()
                .mapToDouble(set -> strengthScoreCalculator.calculateSetVolume(
                        set.getKg(),
                        set.getReps()
                ))
                .sum();

        double bestOneRepMax = sets.stream()
                .filter(set -> set.getEstimatedOneRepMax() != null)
                .mapToDouble(Sets::getEstimatedOneRepMax)
                .max()
                .orElse(0.0);

        double bodyWeight = workout.getUser()
                .getAdditionalInfo()
                .getKg();

        ExerciseDifficulty difficulty = detail.getExercise().getExerciseDifficulty();

        double points = strengthScoreCalculator.calculateExercisePoints(
                bestOneRepMax,
                bodyWeight,
                totalVolume,
                difficulty
        );

        detail.setVolume(strengthScoreCalculator.round(totalVolume, 2));
        detail.setEstimatedOneRepMax(strengthScoreCalculator.round(bestOneRepMax, 2));
        detail.setExercisePoints(strengthScoreCalculator.round(points, 2));

        Double previousBestOneRepMax = workoutDetailsRepository.findPreviousBestOneRepMax(
                workout.getUser().getId(),
                detail.getExercise().getId(),
                workout.getDate()
        );

        LocalDate previousWorkoutDate = workoutDetailsRepository.findPreviousWorkoutDateForExercise(
                workout.getUser().getId(),
                detail.getExercise().getId(),
                workout.getDate()
        );

        Long daysSincePrevious = null;

        if (previousWorkoutDate != null) {
            daysSincePrevious = ChronoUnit.DAYS.between(previousWorkoutDate, workout.getDate());
        }

        AnomalyResult anomalyResult = anomalyDetector.detect(
                bestOneRepMax,
                previousBestOneRepMax,
                daysSincePrevious
        );

        detail.setSuspicious(anomalyResult.suspicious());
        detail.setAnomalyScore(anomalyResult.anomalyScore());
        detail.setAnomalyReason(anomalyResult.anomalyReason());

        workoutDetailsRepository.save(detail);
    }
}
