package com.strongBeton.strongBeton.dto.stats;

import java.time.LocalDate;

public class WeeklyVolumeDTO {
    private String day;
    private LocalDate date;
    private double volume;

    public WeeklyVolumeDTO() {
    }

    public WeeklyVolumeDTO(String day, LocalDate date, double volume) {
        this.day = day;
        this.date = date;
        this.volume = volume;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
