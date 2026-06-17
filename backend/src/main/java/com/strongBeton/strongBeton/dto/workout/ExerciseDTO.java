package com.strongBeton.strongBeton.dto.workout;

import com.strongBeton.strongBeton.enums.ExerciseDifficulty;
import com.strongBeton.strongBeton.enums.ExerciseEquipment;

public class ExerciseDTO {

    private int id;
    private String name;
    private String muscleGroup;
    private ExerciseDifficulty exerciseDifficulty;
    private ExerciseEquipment exerciseEquipment;
    private String imageUrl;

    public ExerciseDTO() {
    }

    public ExerciseDTO(String name, String muscleGroup, ExerciseDifficulty exerciseDifficulty,
                       ExerciseEquipment exerciseEquipment, String imageUrl) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.exerciseDifficulty = exerciseDifficulty;
        this.exerciseEquipment = exerciseEquipment;
        this.imageUrl = imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public ExerciseDifficulty getExerciseDifficulty() {
        return exerciseDifficulty;
    }

    public void setExerciseDifficulty(ExerciseDifficulty exerciseDifficulty) {
        this.exerciseDifficulty = exerciseDifficulty;
    }

    public ExerciseEquipment getExerciseEquipment() {
        return exerciseEquipment;
    }

    public void setExerciseEquipment(ExerciseEquipment exerciseEquipment) {
        this.exerciseEquipment = exerciseEquipment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ExerciseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
