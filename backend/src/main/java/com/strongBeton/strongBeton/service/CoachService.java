package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface CoachService {

    boolean promoteToCoachAccount(User user);
    boolean inviteToBeACoach(User user, int coachId);
    boolean acceptToBeACoach(User user, int clientId);
    boolean fireACoach(User user, int coachId);
    UUID verifyClientAccess(User user, String username);
    boolean checkWorkoutFromCoach(User coach, UUID uuidClient);
}
