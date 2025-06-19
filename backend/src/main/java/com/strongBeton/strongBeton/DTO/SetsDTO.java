package com.strongBeton.strongBeton.DTO;

public class SetsDTO {

    private int id;

    private int reps;

    private float kg;

    private int setNumber;

    private int workoutDetailsId;

    public SetsDTO() {
    }

    public SetsDTO(int reps, float kg, int setNumber, int workoutDetailsId) {
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

    public int getWorkoutDetailsId() {
        return workoutDetailsId;
    }

    public void setWorkoutDetailsId(int workoutDetailsId) {
        this.workoutDetailsId = workoutDetailsId;
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

    @Override
    public String toString() {
        return "SetsDTO{" +
                "id=" + id +
                ", reps=" + reps +
                ", kg=" + kg +
                ", setNumber=" + setNumber +
                '}';
    }
}
