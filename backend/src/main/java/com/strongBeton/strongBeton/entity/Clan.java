package com.strongBeton.strongBeton.entity;

import com.strongBeton.strongBeton.enums.ClanLeague;
import com.strongBeton.strongBeton.enums.PhotoType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clan")
public class Clan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "logo_url", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private CloudPhoto logoUrl;
    @Column(name = "total_xp")
    private int totalXP;
    @Column(name = "current_league")
    private ClanLeague currLeague;
    @Column(name = "is_invite_only")
    private boolean isInvite;
    @Column(name = "clan_points")
    private int clanPoints;
    @Column(name = "create_at")
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "clan")
    private List<ClanMember> members;


    public Clan() {
    }

    public Clan(String name, String description, CloudPhoto logoUrl,
                int totalXP, ClanLeague currLeague, boolean isInvite,
                int clanPoints, LocalDateTime createdAt) {
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
        this.totalXP = totalXP;
        this.currLeague = currLeague;
        this.isInvite = isInvite;
        this.clanPoints = clanPoints;
        this.createdAt = createdAt;
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

    public CloudPhoto getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(CloudPhoto logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getTotalXP() {
        return totalXP;
    }

    public void setTotalXP(int totalXP) {
        this.totalXP = totalXP;
    }

    public ClanLeague getCurrLeague() {
        return currLeague;
    }

    public void setCurrLeague(ClanLeague currLeague) {
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

    public List<ClanMember> getMembers() {
        return members;
    }

    public void setMembers(List<ClanMember> members) {
        this.members = members;
    }
}
