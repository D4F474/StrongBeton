package com.strongBeton.strongBeton.entity;

import jakarta.persistence.*;

@Entity
@Table(name="muscle_group")
public class MuscleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "muscle_group_name")
    private String muscleGroupName;

    public MuscleGroup() {
    }

    public MuscleGroup(String muscleGroupName) {
        this.muscleGroupName = muscleGroupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMuscleGroupName() {
        return muscleGroupName;
    }

    public void setMuscleGroupName(String muscleGroupName) {
        this.muscleGroupName = muscleGroupName;
    }

    @Override
    public String toString() {
        return "MuscleGroup{" +
                "id=" + id +
                ", muscleGroupName='" + muscleGroupName + '\'' +
                '}';
    }
}
