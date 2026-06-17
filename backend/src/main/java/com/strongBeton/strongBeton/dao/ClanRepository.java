package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.clan.Clan;
import com.strongBeton.strongBeton.enums.ClanRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClanRepository extends JpaRepository<Clan, Integer> {
    //Намира клан по име
    Optional<Clan> findByName(String name);

}
