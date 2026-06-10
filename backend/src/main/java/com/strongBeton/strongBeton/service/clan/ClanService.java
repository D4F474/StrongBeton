package com.strongBeton.strongBeton.service.clan;

import com.strongBeton.strongBeton.dto.clan.ClanDTO;
import com.strongBeton.strongBeton.dto.clan.ClanMemberContributionDTO;
import com.strongBeton.strongBeton.dto.clan.ClanMemberDTO;
import com.strongBeton.strongBeton.entity.user.User;

import java.util.List;
import java.util.UUID;

public interface ClanService {

    ClanDTO createClan(ClanDTO clanDTO, User user);
    ClanDTO getMyClan(User user);
    ClanDTO getClanById(int clanId);
    ClanDTO updateClan(int clanId, UUID userId, ClanDTO clanDTO);
    void deleteClan(int clanId, UUID userId);

    void joinClan(int clanId, User user);
    void leaveClan(int clanId, User user);
    void kickMember(int clanId, UUID targetUserId, User requester);
    void inviteMember(int clanId, UUID targetUserId, UUID requesterId);
    void acceptInvite(int clanId, UUID requesterId, int targetId);
    void declineInvite(int clanId, UUID requesterId, int targetId);

    void promoteMember(int clanId, int targetUserId, UUID requesterId);
    void demoteMember(int clanId, int targetUserId, UUID requesterId);
    void transferLeadership(int clanId, UUID newLeaderUserId, int currentLeaderId);





}
