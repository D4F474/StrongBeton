package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.entity.LeaderBoard;
import com.strongBeton.strongBeton.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LeaderboardRestController {

    LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardRestController(LeaderboardService leaderboardService){
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderBoard")
    public ResponseEntity<List<LeaderBoard>> findLeaderBoard(){
        return ResponseEntity.ok(leaderboardService.findLeaderBoardForUser());
    }
}
