package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.stats.StatsOverviewDTO;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/overview")
    public ResponseEntity<StatsOverviewDTO> getStatsOverview(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(statsService.getStatsOverview(currentUser));
    }
}
