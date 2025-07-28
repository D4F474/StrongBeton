package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.entity.FriendView;
import com.strongBeton.strongBeton.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FriendService {

    void sendInviteRequest(User user, int friendId);
    List<FriendView> getFriendsByUsername(String username);
    void acceptFriend(User user, int friendId);
    void declineFriend(User user, int friendId);
    void removeFriend(User user, int friendId);
}
