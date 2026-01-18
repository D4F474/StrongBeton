package com.strongBeton.strongBeton.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="workout")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="uuid_workout")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinColumn(name="user_uuid")
    private User user;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH
            })
    @JoinColumn(name="workout_uuid")
    private Set<WorkoutDetails> workoutDetails;

    @Column(name = "date")
    private LocalDate date;


    @ManyToOne(fetch = FetchType.LAZY,
                cascade = {CascadeType.DETACH, CascadeType.MERGE,
                CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "workout_template_id")
    private WorkoutTemplate workoutTemplate;

    public Workout() {
    }

    public Workout(User user,
                   Set<WorkoutDetails> workoutDetails,
                   LocalDate date,
                   WorkoutTemplate workoutTemplate) {
        this.user = user;
        this.workoutDetails = workoutDetails;
        this.date = date;
        this.workoutTemplate = workoutTemplate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public WorkoutTemplate getWorkoutTemplate() {
        return workoutTemplate;
    }

    public void setWorkoutTemplate(WorkoutTemplate workoutTemplate) {
        this.workoutTemplate = workoutTemplate;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", user=" + user +
                ", workoutDetails=" + workoutDetails +
                ", date=" + date +
                ", workoutName='" + workoutTemplate + '\'' +
                '}';
    }

}