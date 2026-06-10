package com.strongBeton.strongBeton.dto.clan;

import com.strongBeton.strongBeton.entity.clan.ClanMember;

import java.time.LocalDateTime;
import java.util.List;

public class ClanDTO {

    private int id;

    private String name;

    private String description;

    private String logoUrl;

    private int totalXP;

    private String currLeague;

    private boolean isInvite;

    private int clanPoints;

    private LocalDateTime createdAt;

    private List<ClanMemberDTO> members;

    public ClanDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getTotalXP() {
        return totalXP;
    }

    public void setTotalXP(int totalXP) {
        this.totalXP = totalXP;
    }

    public String getCurrLeague() {
        return currLeague;
    }

    public void setCurrLeague(String currLeague) {
        this.currLeague = currLeague;
    }

    public boolean isInvite() {
        return isInvite;
    }

    public void setInvite(boolean invite) {
        isInvite = invite;
    }

    public int getClanPoints() {
        return clanPoints;
    }

    public void setClanPoints(int clanPoints) {
        this.clanPoints = clanPoints;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ClanMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<ClanMemberDTO> members) {
        this.members = members;
    }
}
