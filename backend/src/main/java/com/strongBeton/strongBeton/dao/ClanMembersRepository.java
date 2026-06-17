package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.clan.Clan;
import com.strongBeton.strongBeton.entity.clan.ClanMember;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.enums.ClanRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClanMembersRepository extends JpaRepository<ClanMember, Integer> {
    //Търси определен клан с обвързан с него потребител
    Optional<ClanMember> findByClanAndUser(Clan clan, User user);

    //Търси определен потребител в кой клан членува
    @Query("""
           SELECT cm
           FROM ClanMember cm
           WHERE cm.user.id = :userId
           """)
    Optional<ClanMember> findByUserId(@Param("userId") int userId);

    //Търси броя членовете в определен клан
    @Query("""
       SELECT COUNT(cm)
       FROM ClanMember cm
       WHERE cm.clan.id = :clanId
       """)
    Integer countMembersByClanId(@Param("clanId") int clanId);

    //Връща бройката активни потребители, които са направили точки към клана.
    @Query("""
            SELECT COUNT(cm)
            FROM ClanMember cm
            WHERE cm.clan.id = :clanId
            AND cm.points > 0
            """)
    Integer countActiveMembersByClanId(@Param("clanId") int clanId);

    //Връща бройката на потребители с определена роля.
    @Query("""
        SELECT COUNT(cm)
        FROM ClanMember cm
        WHERE cm.clan.id = :clanId
        AND cm.clanRoleType <> :excludedRole
        """)
    Integer countMembersByClanIdExcludingRole(
            @Param("clanId") int clanId,
            @Param("excludedRole") ClanRoleType excludedRole
    );

    //Връща бройката активни потребители с тяхната роля
    @Query("""
        SELECT COUNT(cm)
        FROM ClanMember cm
        WHERE cm.clan.id = :clanId
        AND cm.clanRoleType <> :excludedRole
        AND cm.points > 0
        """)
    Integer countActiveMembersByClanIdExcludingRole(
            @Param("clanId") int clanId,
            @Param("excludedRole") ClanRoleType excludedRole
    );

    //Връща потребителите в определен клан с тяхната роля в него.
    List<ClanMember> findByClanAndClanRoleType(Clan clan, ClanRoleType clanRoleType);
}
