package com.strongBeton.strongBeton.dto.clan;

import java.time.LocalDateTime;

public class ClanMemberContributionDTO {
    private int id;
    private int clanId;
    private String username;
    private int points;
    private LocalDateTime date;

    public ClanMemberContributionDTO() {
    }

    public ClanMemberContributionDTO(int id, int clanId, String username, int points, LocalDateTime date) {
        this.id = id;
        this.clanId = clanId;
        this.username = username;
        this.points = points;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClanId() {
        return clanId;
    }

    public void setClanId(int clanId) {
        this.clanId = clanId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
