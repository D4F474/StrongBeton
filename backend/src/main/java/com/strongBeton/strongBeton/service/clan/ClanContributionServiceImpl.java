package com.strongBeton.strongBeton.service.clan;

import com.strongBeton.strongBeton.dao.ClanMemberContributionRepository;
import com.strongBeton.strongBeton.dao.ClanMembersRepository;
import com.strongBeton.strongBeton.dao.ClanRepository;
import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.entity.clan.Clan;
import com.strongBeton.strongBeton.entity.clan.ClanMember;
import com.strongBeton.strongBeton.entity.clan.ClanMemberContribution;
import com.strongBeton.strongBeton.entity.workout.Workout;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service

public class ClanContributionServiceImpl implements ClanContributionService{

    private final ClanMembersRepository clanMemberRepository;
    private final ClanMemberContributionRepository contributionRepository;
    private final WorkoutDetailsRepository workoutDetailsRepository;
    private final ClanRepository clanRepository;

    public ClanContributionServiceImpl(
            ClanMembersRepository clanMemberRepository,
            ClanMemberContributionRepository contributionRepository,
            WorkoutDetailsRepository workoutDetailsRepository,
            ClanRepository clanRepository
    ) {
        this.clanMemberRepository = clanMemberRepository;
        this.contributionRepository = contributionRepository;
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.clanRepository = clanRepository;
    }

    @Transactional
    public void addContributionForFinishedWorkout(Workout workout) {
        Optional<ClanMember> clanMemberOptional =
                clanMemberRepository.findByUserId(workout.getUser().getId());

        if (clanMemberOptional.isEmpty()) {
            return;
        }

        Double workoutScore = workoutDetailsRepository.getWorkoutScore(workout.getId());

        if (workoutScore == null || workoutScore <= 0) {
            return;
        }

        int contributionPoints = (int) Math.round(workoutScore);

        ClanMember clanMember = clanMemberOptional.get();
        Clan clan = clanMember.getClan();

        ClanMemberContribution contribution = new ClanMemberContribution();
        contribution.setClan(clan);
        contribution.setUser(workout.getUser());
        contribution.setPoints(contributionPoints);
        contribution.setDate(LocalDateTime.now());

        contributionRepository.save(contribution);

        clanMember.setPoints(clanMember.getPoints() + contributionPoints);
        clanMemberRepository.save(clanMember);

        clan.setClanPoints(clan.getClanPoints() + contributionPoints);
        clanRepository.save(clan);
    }
}
