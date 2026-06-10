package com.strongBeton.strongBeton.service.clan;

import com.strongBeton.strongBeton.dao.ClanMemberContributionRepository;
import com.strongBeton.strongBeton.dao.ClanMembersRepository;
import com.strongBeton.strongBeton.dao.ClanRepository;
import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.dto.clan.ClanMemberContributionDTO;
import com.strongBeton.strongBeton.entity.clan.Clan;
import com.strongBeton.strongBeton.entity.clan.ClanMember;
import com.strongBeton.strongBeton.entity.clan.ClanMemberContribution;
import com.strongBeton.strongBeton.entity.workout.Workout;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class ClanContributionServiceImpl implements ClanContributionService{

    private final ClanMembersRepository clanMemberRepository;
    private final ClanMemberContributionRepository contributionRepository;
    private final WorkoutDetailsRepository workoutDetailsRepository;
    private final ClanRepository clanRepository;
    private static final int DAILY_CLAN_POINTS_CAP = 200;
    private static final int MAX_COUNTED_WORKOUTS_PER_DAY = 1;

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

        ClanMember clanMember = clanMemberOptional.get();
        Clan clan = clanMember.getClan();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfNextDay = startOfDay.plusDays(1);

        Long countedWorkoutsToday = contributionRepository
                .countContributionsByUserAndClanBetween(
                        clan.getId(),
                        workout.getUser().getId(),
                        startOfDay,
                        startOfNextDay
                );

        if (countedWorkoutsToday != null &&
                countedWorkoutsToday >= MAX_COUNTED_WORKOUTS_PER_DAY) {
            return;
        }

        Long pointsToday = contributionRepository
                .sumPointsByUserAndClanBetween(
                        clan.getId(),
                        workout.getUser().getId(),
                        startOfDay,
                        startOfNextDay
                );

        int alreadyEarnedToday = pointsToday != null ? pointsToday.intValue() : 0;
        int remainingDailyPoints = DAILY_CLAN_POINTS_CAP - alreadyEarnedToday;

        if (remainingDailyPoints <= 0) {
            return;
        }

        int contributionPoints = (int) Math.round(workoutScore);
        contributionPoints = Math.min(contributionPoints, remainingDailyPoints);

        if (contributionPoints <= 0) {
            return;
        }

        ClanMemberContribution contribution = new ClanMemberContribution();
        contribution.setClan(clan);
        contribution.setUser(workout.getUser());
        contribution.setPoints(contributionPoints);
        contribution.setDate(now);
        contribution.setCreatedAt(now);

        contributionRepository.save(contribution);

        clanMember.setPoints(clanMember.getPoints() + contributionPoints);
        clanMemberRepository.save(clanMember);

        clan.setClanPoints(clan.getClanPoints() + contributionPoints);
        clanRepository.save(clan);
    }

    @Override
    @Transactional
    public List<ClanMemberContributionDTO> getRecentContributions(int clanId) {
        clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Clan not found"));

        return contributionRepository
                .findRecentByClanId(clanId, PageRequest.of(0, 20))
                .stream()
                .map(this::mapContributionToDTO)
                .toList();
    }

    private ClanMemberContributionDTO mapContributionToDTO(ClanMemberContribution contribution) {
        ClanMemberContributionDTO dto = new ClanMemberContributionDTO();

        dto.setClanId(contribution.getClan().getId());

        if (contribution.getUser() != null) {
            String username = contribution.getUser().getUsername();

            if (username != null && !username.isBlank()) {
                dto.setUsername(username);
            } else {
                dto.setUsername(contribution.getUser().getEmail());
            }
        } else {
            dto.setUsername("Athlete");
        }

        dto.setPoints(contribution.getPoints());
        dto.setDate(contribution.getDate());

        return dto;
    }

}
