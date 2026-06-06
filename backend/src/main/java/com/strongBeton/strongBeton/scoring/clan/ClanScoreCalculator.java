package com.strongBeton.strongBeton.scoring.clan;

import org.springframework.stereotype.Component;

@Component
public class ClanScoreCalculator {

    public double calculateTeamScore(int totalPoints, int totalMembers, int activeMembers) {
        if (totalPoints <= 0 || totalMembers <= 0 || activeMembers <= 0) {
            return 0.0;
        }

        if (activeMembers > totalMembers) {
            activeMembers = totalMembers;
        }

        double averagePoints = (double) totalPoints / totalMembers;
        double sizeBonus = Math.sqrt(totalMembers);
        double activityFactor = (double) activeMembers / totalMembers;

        return averagePoints * sizeBonus * activityFactor;
    }

    public double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
