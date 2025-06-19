package com.strongBeton.strongBeton.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="workout_details")
public class WorkoutDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    //TODO Have to change it to LAZY
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="muscle_group_id")
    private MuscleGroup muscleGroup;

    @Column(name="workout_id")
    private int workoutId;



    public WorkoutDetails() {
    }

    public WorkoutDetails(Exercise exercise,
                          List<Sets> sets) {
        this.exercise = exercise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }


    public int getWorkoutId() {
        return workoutId;
    }

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    @Override
    public String toString() {
        return "WorkoutDetails{" +
                "id=" + id +
                ", exercise=" + exercise +
                ", muscleGroup=" + muscleGroup +
                ", workoutId=" + workoutId +
                '}';
    }
}
