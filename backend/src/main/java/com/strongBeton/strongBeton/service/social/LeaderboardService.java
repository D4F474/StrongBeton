package com.strongBeton.strongBeton.service.social;

import com.strongBeton.strongBeton.entity.LeaderBoard;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeaderboardService {

    List<LeaderBoard> findLeaderBoardForUser();
}
