package com.strongBeton.strongBeton.entity;

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
    @Column(name="Counter_Trainings")
    private int trainingCounter;
    @Column(name="Counter_This_Month_Tranings")
    private int trainingCounterThisMonth;

    public UserTrainingDetails() {
    }

    public UserTrainingDetails(float totalTonnage_kg, float totalTonnageKgThisMonth, int trainingCounter, int trainingCounterThisMonth) {
        this.totalTonnage_kg = totalTonnage_kg;
        this.totalTonnageKgThisMonth = totalTonnageKgThisMonth;
        this.trainingCounter = trainingCounter;
        this.trainingCounterThisMonth = trainingCounterThisMonth;
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
}
