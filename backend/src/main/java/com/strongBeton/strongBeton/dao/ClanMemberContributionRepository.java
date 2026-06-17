package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.clan.ClanMemberContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ClanMemberContributionRepository extends JpaRepository<ClanMemberContribution, Integer> {

    // Взима последните приноси към даден клан, заедно с потребителя и клана, подредени от най-новите към най-старите.
    @Query("""
            SELECT c
            FROM ClanMemberContribution c
            JOIN FETCH c.user
            JOIN FETCH c.clan
            WHERE c.clan.id = :clanId
            ORDER BY c.date DESC
            """)
    List<ClanMemberContribution> findRecentByClanId(
            @Param("clanId") int clanId,
            Pageable pageable
    );

    // Изчислява общия брой точки, спечелени от конкретен потребител в конкретен клан за даден период
    @Query("""
            SELECT COALESCE(SUM(c.points), 0)
            FROM ClanMemberContribution c
            WHERE c.clan.id = :clanId
            AND c.user.id = :userId
            AND c.date >= :from
            AND c.date < :to
            """)
    Long sumPointsByUserAndClanBetween(
            @Param("clanId") int clanId,
            @Param("userId") int userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // Брои колко приноса има конкретен потребител в конкретен клан за даден период.
    @Query("""
            SELECT COUNT(c)
            FROM ClanMemberContribution c
            WHERE c.clan.id = :clanId
            AND c.user.id = :userId
            AND c.date >= :from
            AND c.date < :to
            """)
    Long countContributionsByUserAndClanBetween(
            @Param("clanId") int clanId,
            @Param("userId") int userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
