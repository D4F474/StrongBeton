package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.ClanDTO;
import com.strongBeton.strongBeton.DTO.ClanMemberContributionDTO;
import com.strongBeton.strongBeton.DTO.ClanMemberDTO;
import com.strongBeton.strongBeton.entity.User;

import java.util.List;
import java.util.UUID;

public interface ClanService {

    ClanDTO createClan(ClanDTO clanDTO, User user);
    ClanDTO getClanById(int clanId);
    ClanDTO updateClan(int clanId, UUID userId, ClanDTO clanDTO);
    void deleteClan(int clanId, UUID userId);

    void joinClan(int clanId, UUID userId);
    void leaveClan(int clanId, int userId);
    void kickMember(int clanId, UUID targetUserId, User requester);
    void inviteMember(int clanId, UUID targetUserId, UUID requesterId);
    void acceptInvite(int clanId, UUID requesterId, int targetId);
    void declineInvite(int clanId, UUID requesterId, int targetId);

    void promoteMember(int clanId, int targetUserId, UUID requesterId);
    void demoteMember(int clanId, int targetUserId, UUID requesterId);
    void transferLeadership(int clanId, UUID newLeaderUserId, int currentLeaderId);

    void addPointsToClan(int clanId, UUID userId, int points);
    void updateClanLeague(int clanId);

    List<ClanMemberDTO> getClanMembers(int clanId);
    List<ClanDTO> searchClans(String name);
    List<ClanDTO> getTopClans();
    List<ClanMemberContributionDTO> getMemberContributions(int clanId);


}
