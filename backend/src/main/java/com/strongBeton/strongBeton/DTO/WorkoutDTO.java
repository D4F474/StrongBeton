package com.strongBeton.strongBeton.DTO;

import com.strongBeton.strongBeton.entity.WorkoutDetails;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class WorkoutDTO {
    private UUID id;

    private LocalDate date;

    private String workoutName;

    private Set<WorkoutDetailsDTO> workoutDetails;

    private UserDTO user;

    private double total_tonnage_kg;

    public WorkoutDTO() {
    }

    public WorkoutDTO( UserDTO user, Set<WorkoutDetailsDTO> workoutDetails,LocalDate date, String workoutName) {
        this.date = date;
        this.workoutName = workoutName;
        this.workoutDetails = workoutDetails;
        this.user = user;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public Set<WorkoutDetailsDTO> getWorkoutDetails() {
        return workoutDetails;
    }

    public void setWorkoutDetails(Set<WorkoutDetailsDTO> workoutDetails) {
        this.workoutDetails = workoutDetails;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }



    @Override
    public String toString() {
        return "WorkoutDTO{" +
                "id=" + id +
                ", date=" + date +
                ", workoutName='" + workoutName + '\'' +
                ", workoutDetails=" + workoutDetails +
                ", user=" + user +
                ", total_tonnage_kg=" + total_tonnage_kg +
                '}';
    }

    public double getTotal_tonnage_kg() {
        return total_tonnage_kg;
    }

    public void setTotal_tonnage_kg(double total_tonnage_kg) {
        this.total_tonnage_kg = total_tonnage_kg;
    }

}