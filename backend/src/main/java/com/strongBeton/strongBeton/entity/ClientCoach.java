package com.strongBeton.strongBeton.entity;

import com.strongBeton.strongBeton.enums.CoachStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "client_coach")
public class ClientCoach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id")
    private Coach coach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    @Column(name = "status")
    private CoachStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endData;

    public ClientCoach() {
    }

    public ClientCoach(Coach coach, User user, CoachStatus status, LocalDate startDate, LocalDate endData) {
        this.coach = coach;
        this.client = user;
        this.status = status;
        this.startDate = startDate;
        this.endData = endData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public CoachStatus getStatus() {
        return status;
    }

    public void setStatus(CoachStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndData() {
        return endData;
    }

    public void setEndData(LocalDate endData) {
        this.endData = endData;
    }

    @Override
    public String toString() {
        return "ClientCoach{" +
                "id=" + id +
                ", coach=" + coach +
                ", client=" + client +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endData=" + endData +
                '}';
    }
}
