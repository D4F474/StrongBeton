package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByUuid(UUID uuid);
    @Query(value = "SELECT username FROM user", nativeQuery = true)
    Optional<List<String>> findAllUsername();
    @Query("SELECT u.id FROM User u WHERE u.username IN :usernames")
    List<Integer> findIdsByUsername(@Param("usernames") List<String> usernames);


}
