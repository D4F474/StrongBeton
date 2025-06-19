package com.strongBeton.strongBeton.entity;


import jakarta.persistence.*;

@Entity
@Table(name="sets")
public class Sets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_sets")
    private int id;

    @Column(name="reps")
    private int reps;

    @Column(name="kg")
    private float kg;

    @Column(name="set_number")
    private int setNumber;

    @Column(name="workout_details_id")
    private int workoutDetailsId;

    public Sets() {
    }

    public Sets(int reps,
                float kg,
                int setNumber,
                int workoutDetailsId) {
        this.reps = reps;
        this.kg = kg;
        this.setNumber = setNumber;
        this.workoutDetailsId = workoutDetailsId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public float getKg() {
        return kg;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getWorkoutDetailsId() {
        return workoutDetailsId;
    }

    public void setWorkoutDetailsId(int workoutDetailsId) {
        this.workoutDetailsId = workoutDetailsId;
    }

    @Override
    public String toString() {
        return "Sets{" +
                "id=" + id +
                ", reps=" + reps +
                ", kg=" + kg +
                ", setNumber=" + setNumber +
                ", workoutDetailsId=" + workoutDetailsId +
                '}';
    }
}
