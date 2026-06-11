package com.strongBeton.strongBeton.entity.clan;

import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.enums.ClanRoleType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clan_members")
public class ClanMember {
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
    @Column(name = "clan_role")
    @Enumerated(EnumType.STRING)
    private ClanRoleType clanRoleType;
    @Column(name = "points")
    private int points;
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public ClanMember() {
    }

    public ClanMember(Clan clan, User user, ClanRoleType clanRoleType, int points, LocalDateTime joinderAt, LocalDateTime updatedAt) {
        this.clan = clan;
        this.user = user;
        this.clanRoleType = clanRoleType;
        this.points = points;
        this.joinedAt = joinderAt;
        this.updatedAt = updatedAt;
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

    public ClanRoleType getClanRoleType() {
        return clanRoleType;
    }

    public void setClanRoleType(ClanRoleType clanRoleType) {
        this.clanRoleType = clanRoleType;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getJoinderAt() {
        return joinedAt;
    }

    public void setJoinderAt(LocalDateTime joinderAt) {
        this.joinedAt = joinderAt;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
