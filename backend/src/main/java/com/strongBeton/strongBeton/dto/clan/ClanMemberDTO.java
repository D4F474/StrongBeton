package com.strongBeton.strongBeton.dto.clan;

import java.time.LocalDateTime;

public class ClanMemberDTO {
    private int id;
    private String username;
    private int clanId;
    private String clanRoleType;
    private int points;
    private LocalDateTime joinedAt;

    public ClanMemberDTO() {
    }

    public ClanMemberDTO(int id, String user, int clanId ,String clanRoleType, int points, LocalDateTime joinedAt) {
        this.id = id;
        this.username = user;
        this.clanId = clanId;
        this.clanRoleType = clanRoleType;
        this.points = points;
        this.joinedAt = joinedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return username;
    }

    public void setUser(String user) {
        this.username = user;
    }

    public String getClanRoleType() {
        return clanRoleType;
    }

    public void setClanRoleType(String clanRoleType) {
        this.clanRoleType = clanRoleType;
    }

    public int getClanId() {
        return clanId;
    }

    public void setClanId(int clanId) {
        this.clanId = clanId;
    }

    public int getPoints() {
        return points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
