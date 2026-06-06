package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.workout.WorkoutDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutDetailsRepository extends JpaRepository<WorkoutDetails, Integer> {
    WorkoutDetails findExerciseById(int theId);

    @Query(value = "SELECT * FROM workout_details WHERE workout_uuid =?1", nativeQuery = true)
    List<WorkoutDetails> findByWorkoutId(UUID workoutId);

    @Query("SELECT COALESCE(SUM(wd.exercisePoints), 0) FROM WorkoutDetails wd WHERE wd.workoutId = :workoutId")
    Double getWorkoutScore(@Param("workoutId") UUID workoutId);

    @Query(value = """
        SELECT COALESCE(SUM(wd.exercise_points), 0)
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        WHERE w.user_id = :userId
        """, nativeQuery = true)
    Double getTotalScoreByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT COALESCE(SUM(wd.volume), 0)
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        WHERE w.user_id = :userId
        """, nativeQuery = true)
    Double getTotalVolumeByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT COALESCE(MAX(wd.estimated_1rm), 0)
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        WHERE w.user_id = :userId
        """, nativeQuery = true)
    Double getBestEstimatedOneRepMaxByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT e.name
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        JOIN exercise e ON e.id = wd.exercise_id
        WHERE w.user_id = :userId
        GROUP BY e.id, e.name
        ORDER BY COUNT(*) DESC
        LIMIT 1
        """, nativeQuery = true)
    String getMostUsedExerciseNameByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT COUNT(*)
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        WHERE w.user_id = :userId
        GROUP BY wd.exercise_id
        ORDER BY COUNT(*) DESC
        LIMIT 1
        """, nativeQuery = true)
    Integer getMostUsedExerciseCountByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT e.name
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        JOIN exercise e ON e.id = wd.exercise_id
        WHERE w.user_id = :userId
        ORDER BY wd.estimated_1rm DESC
        LIMIT 1
        """, nativeQuery = true)
    String getBestExerciseNameByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT COALESCE(SUM(wd.volume), 0)
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        WHERE w.user_id = :userId
        AND MONTH(w.date) = MONTH(CURRENT_DATE())
        AND YEAR(w.date) = YEAR(CURRENT_DATE())
        """, nativeQuery = true)
    Double getTotalVolumeThisMonthByUserId(@Param("userId") int userId);


    @Query("SELECT COALESCE(SUM(wd.volume), 0) FROM WorkoutDetails wd WHERE wd.workoutId = :workoutId")
    Double getWorkoutVolume(@Param("workoutId") UUID workoutId);

    @Query(value = """
        SELECT COALESCE(SUM(wd.exercise_points), 0)
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        WHERE w.user_id = :userId
        AND MONTH(w.date) = MONTH(CURRENT_DATE())
        AND YEAR(w.date) = YEAR(CURRENT_DATE())
        """, nativeQuery = true)
    Double getTotalScoreThisMonthByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT 
            DATEDIFF(daily.date, first_dates.first_date) AS day_index,
            daily.best_1rm
        FROM (
            SELECT 
                w.date AS date,
                MAX(wd.estimated_1rm) AS best_1rm
            FROM workout_details wd
            JOIN workout w ON w.uuid_workout = wd.workout_uuid
            WHERE w.user_id = :userId
            AND wd.exercise_id = :exerciseId
            AND wd.estimated_1rm IS NOT NULL
            AND wd.estimated_1rm > 0
            GROUP BY w.date
        ) daily
        JOIN (
            SELECT MIN(w2.date) AS first_date
            FROM workout_details wd2
            JOIN workout w2 ON w2.uuid_workout = wd2.workout_uuid
            WHERE w2.user_id = :userId
            AND wd2.exercise_id = :exerciseId
            AND wd2.estimated_1rm IS NOT NULL
            AND wd2.estimated_1rm > 0
        ) first_dates
        ORDER BY daily.date ASC
        """, nativeQuery = true)
    List<Object[]> findDailyBestProgressPointsByUserAndExercise(
            @Param("userId") int userId,
            @Param("exerciseId") int exerciseId
    );

    @Query("""
       SELECT wd
       FROM WorkoutDetails wd
       WHERE wd.workoutId = :workoutId
       AND wd.exercise.id = :exerciseId
       """)
    Optional<WorkoutDetails> findExistingWorkoutDetail(
            @Param("workoutId") UUID workoutId,
            @Param("exerciseId") int exerciseId
    );

    @Query(value = """
        SELECT MAX(wd.estimated_1rm)
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        WHERE w.user_id = :userId
          AND wd.exercise_id = :exerciseId
          AND w.date < :currentWorkoutDate
          AND wd.estimated_1rm IS NOT NULL
          AND wd.estimated_1rm > 0
        """, nativeQuery = true)
    Double findPreviousBestOneRepMax(
            @Param("userId") int userId,
            @Param("exerciseId") int exerciseId,
            @Param("currentWorkoutDate") LocalDate currentWorkoutDate
    );

    @Query(value = """
        SELECT MAX(w.date)
        FROM workout_details wd
        JOIN workout w ON w.uuid_workout = wd.workout_uuid
        WHERE w.user_id = :userId
          AND wd.exercise_id = :exerciseId
          AND w.date < :currentWorkoutDate
          AND wd.estimated_1rm IS NOT NULL
          AND wd.estimated_1rm > 0
        """, nativeQuery = true)
    LocalDate findPreviousWorkoutDateForExercise(
            @Param("userId") int userId,
            @Param("exerciseId") int exerciseId,
            @Param("currentWorkoutDate") LocalDate currentWorkoutDate
    );

    void deleteByExerciseId(int theId);
}
