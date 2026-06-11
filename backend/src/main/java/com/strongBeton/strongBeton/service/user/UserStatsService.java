package com.strongBeton.strongBeton.service.user;

import com.strongBeton.strongBeton.dto.user.UserStatsDTO;

import java.util.UUID;

public interface UserStatsService {
    UserStatsDTO getStats(UUID userUuid);
}
