package com.strongBeton.strongBeton.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "leader_board_view")
@Immutable
public class LeaderBoard {

    @Id
    @Column(name="id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "workout_counter")
    private int workoutCounter;

    @Column(name = "sum_kg")
    private float sumKg;

    public LeaderBoard() {
    }

    public LeaderBoard(String username, int workoutCounter, float sumKg) {
        this.username = username;
        this.workoutCounter = workoutCounter;
        this.sumKg = sumKg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWorkoutCounter() {
        return workoutCounter;
    }

    public void setWorkoutCounter(int workoutCounter) {
        this.workoutCounter = workoutCounter;
    }

    public float getSumKg() {
        return sumKg;
    }

    public void setSumKg(float sumKg) {
        this.sumKg = sumKg;
    }

    @Override
    public String toString() {
        return "LeaderBoard{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", workoutCounter=" + workoutCounter +
                ", sumKg=" + sumKg +
                '}';
    }
}
