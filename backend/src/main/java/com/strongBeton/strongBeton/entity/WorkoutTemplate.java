package com.strongBeton.strongBeton.entity;

import jakarta.persistence.*;

@Entity
@Table(name="workout_template")
public class WorkoutTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name= "id")
    private int id;

    @Column(name = "workout_name")
    private String workout_name;

    public WorkoutTemplate() {
    }

    public WorkoutTemplate(String workout_name) {
        this.workout_name = workout_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkout_name() {
        return workout_name;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }
}
