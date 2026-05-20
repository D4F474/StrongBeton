package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.Clan;
import com.strongBeton.strongBeton.entity.ClanMember;
import com.strongBeton.strongBeton.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClanMembersRepository extends JpaRepository<ClanMember, Integer> {

    Optional<ClanMember> findByClanAndUser(Clan clan, User user);
}
