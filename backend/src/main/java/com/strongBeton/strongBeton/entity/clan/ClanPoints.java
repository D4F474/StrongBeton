package com.strongBeton.strongBeton.entity.clan;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clan_points")
public class ClanPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "clan_id", nullable = false)
    private Clan clan;
    @Column(name = "clan_points")
    private int clanPoints;
    @Column(name = "date")
    private LocalDateTime date;

    public ClanPoints() {
    }

    public ClanPoints(Clan clan, int clanPoints, LocalDateTime date) {
        this.clan = clan;
        this.clanPoints = clanPoints;
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

    public int getClanPoints() {
        return clanPoints;
    }

    public void setClanPoints(int clanPoints) {
        this.clanPoints = clanPoints;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
