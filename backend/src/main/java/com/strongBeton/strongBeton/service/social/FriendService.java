package com.strongBeton.strongBeton.service.social;

import com.strongBeton.strongBeton.dto.user.FriendViewDTO;
import com.strongBeton.strongBeton.dto.user.UserStatusDTO;
import com.strongBeton.strongBeton.entity.social.FriendView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface FriendService {

    void sendInviteRequest(int userId, String username);
    List<FriendViewDTO> getFriendsByUsername(String username);
    void acceptFriend(int userId, String username);
    void declineFriend(int userId, String username);
    void removeFriend(int userId, String username);

    Set<UserStatusDTO> getUsernames(String userUsername);
}
