package com.strongBeton.strongBeton.dto.workout;

import java.util.UUID;

public class ActiveWorkoutPreviewDTO {

    private UUID id;
    private String name;
    private String currentExercise;

    public ActiveWorkoutPreviewDTO(UUID id, String name, String currentExercise) {
        this.id = id;
        this.name = name;
        this.currentExercise = currentExercise;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentExercise() {
        return currentExercise;
    }

    public void setCurrentExercise(String currentExercise) {
        this.currentExercise = currentExercise;
    }
}
