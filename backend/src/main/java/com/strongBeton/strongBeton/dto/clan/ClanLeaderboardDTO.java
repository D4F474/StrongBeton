package com.strongBeton.strongBeton.dto.clan;

public class ClanLeaderboardDTO {
    private int clanId;
    private String clanName;
    private String logoUrl;

    private int totalMembers;
    private int activeMembers;

    private int totalPoints;
    private Double teamScore;

    public ClanLeaderboardDTO() {
    }

    public int getClanId() {
        return clanId;
    }

    public void setClanId(int clanId) {
        this.clanId = clanId;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int getActiveMembers() {
        return activeMembers;
    }

    public void setActiveMembers(int activeMembers) {
        this.activeMembers = activeMembers;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Double getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(Double teamScore) {
        this.teamScore = teamScore;
    }
}
