package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.UserTrainingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserTrainingDetailsRepository extends JpaRepository<UserTrainingDetails, UUID> {
}
