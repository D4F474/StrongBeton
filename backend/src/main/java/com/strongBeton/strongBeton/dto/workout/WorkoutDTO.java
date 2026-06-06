package com.strongBeton.strongBeton.dto.workout;

import com.strongBeton.strongBeton.dto.user.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WorkoutDTO {
    private UUID id;

    private LocalDate date;

    private String workoutName;

    private List<WorkoutDetailsDTO> workoutDetails;

    private UserDTO user;

    private double total_tonnage_kg;

    private Double workoutScore;

    private Double workoutVolume;

    private String status;

    public WorkoutDTO() {
    }

    public WorkoutDTO(LocalDate date, String workoutName, List<WorkoutDetailsDTO> workoutDetails,
                      UserDTO user, double total_tonnage_kg, Double workoutScore,
                      Double workoutVolume, String status) {
        this.date = date;
        this.workoutName = workoutName;
        this.workoutDetails = workoutDetails;
        this.user = user;
        this.total_tonnage_kg = total_tonnage_kg;
        this.workoutScore = workoutScore;
        this.workoutVolume = workoutVolume;
        this.status = status;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<WorkoutDetailsDTO> getWorkoutDetails() {
        return workoutDetails;
    }

    public void setWorkoutDetails(List<WorkoutDetailsDTO> workoutDetails) {
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

    public Double getWorkoutScore() {
        return workoutScore;
    }

    public void setWorkoutScore(Double workoutScore) {
        this.workoutScore = workoutScore;
    }

    public Double getWorkoutVolume() {
        return workoutVolume;
    }

    public void setWorkoutVolume(Double workoutVolume) {
        this.workoutVolume = workoutVolume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}