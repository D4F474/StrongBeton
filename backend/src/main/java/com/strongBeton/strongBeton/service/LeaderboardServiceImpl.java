package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dao.LeaderBoardRepository;
import com.strongBeton.strongBeton.entity.LeaderBoard;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    private LeaderBoardRepository leaderBoardRepository;

    public LeaderboardServiceImpl(LeaderBoardRepository leaderBoardRepository) {
        this.leaderBoardRepository = leaderBoardRepository;
    }

    @Override
    public List<LeaderBoard> findLeaderBoardForUser(){
        return leaderBoardRepository.findAll();
    }
}
