package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.Clan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClanRepository extends JpaRepository<Clan, Integer> {
    Optional<Clan> findByName(String name);
}
