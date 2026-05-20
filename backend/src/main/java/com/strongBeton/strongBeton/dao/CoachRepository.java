package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.Coach;
import com.strongBeton.strongBeton.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Integer> {
    Optional<Coach> findByUser(User user);

}
