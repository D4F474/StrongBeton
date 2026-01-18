package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.UserStatusDTO;
import com.strongBeton.strongBeton.entity.FriendView;
import com.strongBeton.strongBeton.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public interface FriendService {

    public void sendInviteRequest(UUID userId, String username);
    List<FriendView> getFriendsByUsername(String username);
    void acceptFriend(UUID userId, String username);
    void declineFriend(UUID userId, String username);
    void removeFriend(UUID userId, String username);

    Set<UserStatusDTO> getUsernames(String userUsername);
}
