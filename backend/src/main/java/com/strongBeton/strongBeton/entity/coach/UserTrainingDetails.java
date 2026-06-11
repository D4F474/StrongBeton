package com.strongBeton.strongBeton.entity.coach;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "user_workout_summary_view")
@Immutable
public class UserTrainingDetails {

    @Id
    @Column(name="id_user")
    private int id;
    @Column(name="total_tonnage_kg")
    private float totalTonnage_kg;
    @Column(name="tonnage_this_month_kg")
    private float totalTonnageKgThisMonth;
    @Column(name="total_trainings")
    private int trainingCounter;
    @Column(name="trainings_this_month")
    private int trainingCounterThisMonth;
    @Column(name="most_used_exercise")
    private String exerciseName;
    @Column(name="most_used_exercise_count")
    private int mostUsedExerciseCount;

    public UserTrainingDetails() {
    }

    public UserTrainingDetails(float totalTonnage_kg, float totalTonnageKgThisMonth, int trainingCounter, int trainingCounterThisMonth, String exerciseName, int mostUsedExerciseCount) {
        this.totalTonnage_kg = totalTonnage_kg;
        this.totalTonnageKgThisMonth = totalTonnageKgThisMonth;
        this.trainingCounter = trainingCounter;
        this.trainingCounterThisMonth = trainingCounterThisMonth;
        this.exerciseName = exerciseName;
        this.mostUsedExerciseCount = mostUsedExerciseCount;
    }

    public float getTotalTonnage_kg() {
        return totalTonnage_kg;
    }

    public void setTotalTonnage_kg(float totalTonnage_kg) {
        this.totalTonnage_kg = totalTonnage_kg;
    }

    public float getTotalTonnageKgThisMonth() {
        return totalTonnageKgThisMonth;
    }

    public void setTotalTonnageKgThisMonth(float totalTonnageKgThisMonth) {
        this.totalTonnageKgThisMonth = totalTonnageKgThisMonth;
    }

    public int getTrainingCounter() {
        return trainingCounter;
    }

    public void setTrainingCounter(int trainingCounter) {
        this.trainingCounter = trainingCounter;
    }

    public int getTrainingCounterThisMonth() {
        return trainingCounterThisMonth;
    }

    public void setTrainingCounterThisMonth(int trainingCounterThisMonth) {
        this.trainingCounterThisMonth = trainingCounterThisMonth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getMostUsedExerciseCount() {
        return mostUsedExerciseCount;
    }

    public void setMostUsedExerciseCount(int mostUsedExerciseCount) {
        this.mostUsedExerciseCount = mostUsedExerciseCount;
    }
}
