package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.user.UserStatsDTO;
import com.strongBeton.strongBeton.service.user.UserStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserStatsController {

    private final UserStatsService userStatsService;

    public UserStatsController(UserStatsService userStatsService) {
        this.userStatsService = userStatsService;
    }

    @GetMapping("/{userUuid}/stats")
    public ResponseEntity<UserStatsDTO> getStats(@PathVariable UUID userUuid) {
        return ResponseEntity.ok(userStatsService.getStats(userUuid));
    }
}
