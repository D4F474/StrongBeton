package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dto.stats.StatsOverviewDTO;
import com.strongBeton.strongBeton.entity.user.User;

public interface StatsService {
    StatsOverviewDTO getStatsOverview(User currentUser);
}
