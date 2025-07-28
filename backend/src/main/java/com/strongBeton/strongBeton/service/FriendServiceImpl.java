package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dao.FriendShipRepository;
import com.strongBeton.strongBeton.dao.FriendViewRepository;
import com.strongBeton.strongBeton.entity.FriendShip;
import com.strongBeton.strongBeton.entity.FriendView;
import com.strongBeton.strongBeton.entity.User;
import org.springframework.stereotype.Service;
import com.strongBeton.strongBeton.enums.status;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    FriendShipRepository friendShipRepository;
    FriendViewRepository friendViewRepository;

    public FriendServiceImpl(FriendShipRepository friendShipRepository,
                             FriendViewRepository friendViewRepository) {
        this.friendShipRepository = friendShipRepository;
        this.friendViewRepository  = friendViewRepository;
    }

    @Override
    public List<FriendView> getFriendsByUsername(String username){

        return this.friendViewRepository.findAllFriendsVisual(username);
    }

    @Override
    public void sendInviteRequest(User user, int friendId) {
        this.friendShipRepository.sendingFriendRequest(user.getId(), friendId);
        this.receiveTheRequest(friendId, user.getId());
    }

    private void receiveTheRequest(int friendId, int userId){
        this.friendShipRepository.sendingFriendRequest(friendId, userId);
    }


    @Override
    public void acceptFriend(User user, int friendId) {
      List<FriendShip> friends = findFriends(user.getId());
      if(friends == null) {
          return;
      }

        for(FriendShip friend : friends){
            if(friend.getStatus().getText().equals("Response") && friend.getFriend_id() == friendId){
                this.friendShipRepository.acceptFriendRequest(user.getId(), friend.getFriend_id());
                this.friendShipRepository.acceptFriendRequest(friend.getFriend_id(), user.getId());
                break;
            }
        }
    }

    @Override
    public void declineFriend(User user, int friendId) {
        List<FriendShip> friends = findFriends(user.getId());
        if(friends == null) {
            return;
        }
        for(FriendShip friend : friends){
            if(friend.getStatus().getText().equals("Response")
                    && friend.getFriend_id() == friendId){
                this.friendShipRepository.deleteById(friend.getId());
                this.friendShipRepository.deleteFriendRequest(friend.getFriend_id(), user.getId());
                break;
            }
        }
    }

    private List<FriendShip> findFriends(int userId){
            List<FriendShip> friends = this.friendShipRepository.findByUserId(userId);
            return friends;

    }

    @Override
    public void removeFriend(User user, int friendId) {
        List<FriendShip> friends = this.findFriends(user.getId());
        for(FriendShip friend : friends){
            if(friend.getFriend_id() == friendId){
                this.friendShipRepository.deleteById(friend.getId());
            }
        }
        friends = this.findFriends(friendId);
        for(FriendShip friend : friends){
            if(friend.getFriend_id() == user.getId()){
                this.friendShipRepository.deleteById(friend.getId());
            }
        }
    }

}
