package com.strongBeton.strongBeton.service.clan;

import com.strongBeton.strongBeton.dto.clan.ClanMemberContributionDTO;
import com.strongBeton.strongBeton.entity.workout.Workout;

import java.util.List;

public interface ClanContributionService {
    public void addContributionForFinishedWorkout(Workout workout);
    List<ClanMemberContributionDTO> getRecentContributions(int clanId);
}
