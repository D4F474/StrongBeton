package com.strongBeton.strongBeton.DTO;

import com.strongBeton.strongBeton.entity.Exercise;
import com.strongBeton.strongBeton.entity.MuscleGroup;
import com.strongBeton.strongBeton.entity.Workout;

public class WorkoutDetailsDTO {

    private int id;
    private ExerciseDTO exercise;
    private String muscleGroup;

    public WorkoutDetailsDTO() {
    }

    public WorkoutDetailsDTO( ExerciseDTO exercise, String muscleGroup) {
        this.exercise = exercise;
        this.muscleGroup = muscleGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExerciseDTO getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDTO exercise) {
        this.exercise = exercise;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    @Override
    public String toString() {
        return "WorkoutDetailsDTO{" +
                "id=" + id +
                ", exercise=" + exercise +
                ", muscleGroup=" + muscleGroup +
                '}';
    }
}
