package com.strongBeton.strongBeton.dao.projection;

import java.time.LocalDate;

public interface RecentRecordRow {
    String getExerciseName();
    Float getKg();
    Integer getReps();
    Double getEstimatedOneRepMax();
    LocalDate getWorkoutDate();
}
