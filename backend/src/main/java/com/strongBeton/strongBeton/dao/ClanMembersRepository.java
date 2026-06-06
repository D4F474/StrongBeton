package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.clan.Clan;
import com.strongBeton.strongBeton.entity.clan.ClanMember;
import com.strongBeton.strongBeton.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClanMembersRepository extends JpaRepository<ClanMember, Integer> {

    Optional<ClanMember> findByClanAndUser(Clan clan, User user);

    @Query("""
           SELECT cm
           FROM ClanMember cm
           WHERE cm.user.id = :userId
           """)
    Optional<ClanMember> findByUserId(@Param("userId") int userId);

    @Query("""
       SELECT COUNT(cm)
       FROM ClanMember cm
       WHERE cm.clan.id = :clanId
       """)
    Integer countMembersByClanId(@Param("clanId") int clanId);

    @Query("""
       SELECT COUNT(cm)
       FROM ClanMember cm
       WHERE cm.clan.id = :clanId
       AND cm.points > 0
       """)
    Integer countActiveMembersByClanId(@Param("clanId") int clanId);


}
