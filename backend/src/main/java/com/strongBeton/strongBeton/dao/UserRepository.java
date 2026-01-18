package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT username FROM user", nativeQuery = true)
    Optional<List<String>> findAllUsername();

}
