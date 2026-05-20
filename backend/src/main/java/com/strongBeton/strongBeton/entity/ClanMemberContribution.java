package com.strongBeton.strongBeton.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clan_member_contribution")
public class ClanMemberContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "clan_id")
    private Clan clan;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "points")
    private int points;
    @Column(name = "date")
    private LocalDateTime date;

    public ClanMemberContribution() {
    }

    public ClanMemberContribution(Clan clan, User user, int points, LocalDateTime date) {
        this.clan = clan;
        this.user = user;
        this.points = points;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
