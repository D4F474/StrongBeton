package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.Injuries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InjuriesRepository extends JpaRepository<Injuries, Integer> {

    Optional<List<Injuries>> findAllByUserId(int id);
}
