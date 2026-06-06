package com.strongBeton.strongBeton.service.clan;

import com.strongBeton.strongBeton.dao.ClanMembersRepository;
import com.strongBeton.strongBeton.dao.ClanRepository;
import com.strongBeton.strongBeton.dto.clan.ClanLeaderboardDTO;
import com.strongBeton.strongBeton.entity.clan.Clan;
import com.strongBeton.strongBeton.scoring.clan.ClanScoreCalculator;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ClanLeaderboardServiceImpl implements ClanLeaderboardService{

    private final ClanRepository clanRepository;
    private final ClanMembersRepository clanMemberRepository;
    private final ClanScoreCalculator clanScoreCalculator;

    public ClanLeaderboardServiceImpl(
            ClanRepository clanRepository,
            ClanMembersRepository clanMemberRepository,
            ClanScoreCalculator clanScoreCalculator
    ) {
        this.clanRepository = clanRepository;
        this.clanMemberRepository = clanMemberRepository;
        this.clanScoreCalculator = clanScoreCalculator;
    }

    public List<ClanLeaderboardDTO> getTopClans() {
        return clanRepository.findAll()
                .stream()
                .map(this::mapClanToLeaderboardDTO)
                .sorted(Comparator.comparing(ClanLeaderboardDTO::getTeamScore).reversed())
                .toList();
    }

    private ClanLeaderboardDTO mapClanToLeaderboardDTO(Clan clan) {
        Integer totalMembersResult = clanMemberRepository.countMembersByClanId(clan.getId());
        Integer activeMembersResult = clanMemberRepository.countActiveMembersByClanId(clan.getId());

        int totalMembers = totalMembersResult != null ? totalMembersResult : 0;
        int activeMembers = activeMembersResult != null ? activeMembersResult : 0;
        int totalPoints = clan.getClanPoints();

        double teamScore = clanScoreCalculator.calculateTeamScore(
                totalPoints,
                totalMembers,
                activeMembers
        );

        ClanLeaderboardDTO dto = new ClanLeaderboardDTO();

        dto.setClanId(clan.getId());
        dto.setClanName(clan.getName());
        dto.setLogoUrl(clan.getLogoUrl());
        dto.setTotalMembers(totalMembers);
        dto.setActiveMembers(activeMembers);
        dto.setTotalPoints(totalPoints);
        dto.setTeamScore(clanScoreCalculator.round(teamScore, 2));

        return dto;
    }
}
