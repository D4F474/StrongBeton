package com.strongBeton.strongBeton.service.clan;

import com.strongBeton.strongBeton.dto.clan.ClanLeaderboardDTO;

import java.util.List;

public interface ClanLeaderboardService {
    public List<ClanLeaderboardDTO> getTopClans();
}
