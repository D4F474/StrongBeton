package com.strongBeton.strongBeton.service.user;

import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.dao.WorkoutRepository;
import com.strongBeton.strongBeton.dto.user.UserStatsDTO;
import com.strongBeton.strongBeton.entity.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserStatsServiceImpl implements UserStatsService{

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutDetailsRepository workoutDetailsRepository;

    public UserStatsServiceImpl(
            UserRepository userRepository,
            WorkoutRepository workoutRepository,
            WorkoutDetailsRepository workoutDetailsRepository
    ) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.workoutDetailsRepository = workoutDetailsRepository;
    }

    public UserStatsDTO getStats(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        int userId = user.getId();

        UserStatsDTO dto = new UserStatsDTO();

        dto.setUserId(user.getUuid());
        dto.setUsername(user.getUsername());

        dto.setTotalWorkouts(workoutRepository.countWorkoutsByUserId(userId));
        dto.setTotalWorkoutsThisMonth(workoutRepository.countWorkoutsThisMonthByUserId(userId));

        dto.setTotalVolume(workoutDetailsRepository.getTotalVolumeByUserId(userId));
        dto.setTotalVolumeThisMonth(workoutDetailsRepository.getTotalVolumeThisMonthByUserId(userId));

        dto.setTotalScore(workoutDetailsRepository.getTotalScoreByUserId(userId));
        dto.setTotalScoreThisMonth(workoutDetailsRepository.getTotalScoreThisMonthByUserId(userId));

        dto.setBestEstimatedOneRepMax(workoutDetailsRepository.getBestEstimatedOneRepMaxByUserId(userId));
        dto.setBestExerciseName(workoutDetailsRepository.getBestExerciseNameByUserId(userId));

        String mostUsedName = workoutDetailsRepository.getMostUsedExerciseNameByUserId(userId);
        Integer mostUsedCount = workoutDetailsRepository.getMostUsedExerciseCountByUserId(userId);

        dto.setMostUsedExerciseName(mostUsedName != null ? mostUsedName : null);
        dto.setMostUsedExerciseCount(mostUsedCount != null ? mostUsedCount : 0);

        if (dto.getTotalWorkouts() > 0) {
            dto.setAverageWorkoutScore(dto.getTotalScore() / dto.getTotalWorkouts());
        } else {
            dto.setAverageWorkoutScore(0.0);
        }

        return dto;
    }
}
