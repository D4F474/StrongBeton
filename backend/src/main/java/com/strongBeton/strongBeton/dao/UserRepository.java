package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.UserSuggestionProjection;
import com.strongBeton.strongBeton.entity.user.User;
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
    @Query(value = """
            SELECT
                u.id AS id,
                u.username AS username,
                MAX(p.photo_url) AS profileImageUrl
            FROM user u
            LEFT JOIN photos p
                ON p.user_id = u.id
                AND p.photo_type = 'PROFILE'
                AND p.is_active = true
            GROUP BY u.id, u.username
            """, nativeQuery = true)
    List<UserSuggestionProjection> findAllUsersForSuggestions();


}
