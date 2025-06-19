package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City,Integer> {
    Optional<City> findByCityName(String cityName);
}
