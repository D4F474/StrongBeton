package com.strongBeton.strongBeton.dao.projection;

import java.time.LocalDate;

public interface DailyVolumeRow {
    LocalDate getWorkoutDate();
    Double getVolume();
}
