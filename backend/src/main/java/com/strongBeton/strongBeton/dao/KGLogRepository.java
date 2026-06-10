package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.user.KGLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KGLogRepository extends JpaRepository<KGLogs,Integer> {
    Optional<KGLogs> findTopByUserIdOrderByLoggedAtDesc(Integer userId);
}
