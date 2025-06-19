package com.strongBeton.strongBeton.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="workout")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_workout")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH
            })
    @JoinColumn(name="workout_id")
    private Set<WorkoutDetails> workoutDetails;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "workout_name")
    private String workoutName;

    public Workout() {
    }

    public Workout(User user,
                   Set<WorkoutDetails> workoutDetails,
                   LocalDate date,
                   String workoutName) {
        this.user = user;
        this.workoutDetails = workoutDetails;
        this.date = date;
        this.workoutName = workoutName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<WorkoutDetails> getWorkoutDetails() {
        return workoutDetails;
    }

    public void setWorkoutDetails(Set<WorkoutDetails> workoutDetails) {
        this.workoutDetails = workoutDetails;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", user=" + user +
                ", workoutDetails=" + workoutDetails +
                ", date=" + date +
                ", workoutName='" + workoutName + '\'' +
                '}';
    }

}