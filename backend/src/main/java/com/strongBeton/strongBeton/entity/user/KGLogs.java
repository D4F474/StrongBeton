package com.strongBeton.strongBeton.entity.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "kg_log")
public class KGLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "kg")
    private float kg;
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                     CascadeType.REFRESH
            })
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "logged_at")
    private LocalDateTime loggedAt;

    public KGLogs() {
    }

    public KGLogs(float kg, User user, LocalDateTime createdAt, LocalDateTime loggedAt) {
        this.kg = kg;
        this.user = user;
        this.createdAt = createdAt;
        this.loggedAt = loggedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getKg() {
        return kg;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}
