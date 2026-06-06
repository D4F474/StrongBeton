package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dao.ExerciseRepository;
import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.dto.ProgressPredictionDTO;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.entity.workout.Exercise;
import com.strongBeton.strongBeton.scoring.progress.LinearRegressionResult;
import com.strongBeton.strongBeton.scoring.progress.ProgressPoint;
import com.strongBeton.strongBeton.scoring.progress.ProgressPredictor;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProgressServiceImpl implements ProgressService {
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutDetailsRepository workoutDetailsRepository;
    private final ProgressPredictor progressPredictor;

    public ProgressServiceImpl(
            UserRepository userRepository,
            ExerciseRepository exerciseRepository,
            WorkoutDetailsRepository workoutDetailsRepository,
            ProgressPredictor progressPredictor
    ) {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.progressPredictor = progressPredictor;
    }

    public ProgressPredictionDTO predictExerciseProgress(UUID userUuid, int exerciseId) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        List<Object[]> rawPoints = workoutDetailsRepository.findDailyBestProgressPointsByUserAndExercise(
                user.getId(),
                exerciseId
        );

        List<ProgressPoint> points = new ArrayList<>();

        for (Object[] row : rawPoints) {
            Number dayIndexNumber = (Number) row[0];
            Number estimatedOneRmNumber = (Number) row[1];

            double x = dayIndexNumber.doubleValue();
            double y = estimatedOneRmNumber.doubleValue();

            points.add(new ProgressPoint(x, y));
        }

        ProgressPredictionDTO dto = new ProgressPredictionDTO();

        dto.setExerciseId(exercise.getId());
        dto.setExerciseName(exercise.getName());
        dto.setDataPoints(points.size());

        if (points.isEmpty()) {
            dto.setCurrentEstimatedOneRepMax(0.0);
            dto.setPredictedOneRepMaxAfter30Days(0.0);
            dto.setBeta0(0.0);
            dto.setBeta1(0.0);
            dto.setTrend("UNKNOWN");
            dto.setReliable(false);
            return dto;
        }

        double currentOneRm = points.get(points.size() - 1).y();
        double lastX = points.get(points.size() - 1).x();

        LinearRegressionResult result = progressPredictor.predict(points);

        dto.setCurrentEstimatedOneRepMax(progressPredictor.round(currentOneRm, 2));
        dto.setBeta0(progressPredictor.round(result.beta0(), 4));
        dto.setBeta1(progressPredictor.round(result.beta1(), 4));
        dto.setReliable(result.reliable());

        if (!result.reliable()) {
            dto.setPredictedOneRepMaxAfter30Days(progressPredictor.round(currentOneRm, 2));
            dto.setTrend("NOT_ENOUGH_DATA");
            return dto;
        }

        double predicted = result.predict(lastX + 30);

        dto.setPredictedOneRepMaxAfter30Days(progressPredictor.round(predicted, 2));
        dto.setTrend(progressPredictor.classifyTrend(result.beta1()));

        return dto;
    }
}
