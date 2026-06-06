package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.coach.ClientCoach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientCoachRepository   extends JpaRepository<ClientCoach, Integer> {

    @Query(value = "SELECT * FROM client_coach WHERE client_id = :clientId AND coach_id = :coachId",
            nativeQuery = true)
    Optional<ClientCoach> findByClientAndCoach(int clientId, int coachId);
}
