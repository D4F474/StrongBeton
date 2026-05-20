package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.UserStatusDTO;
import com.strongBeton.strongBeton.dao.FriendShipRepository;
import com.strongBeton.strongBeton.dao.FriendViewRepository;
import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.entity.FriendShip;
import com.strongBeton.strongBeton.entity.FriendView;
import com.strongBeton.strongBeton.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.*;

import static com.strongBeton.strongBeton.enums.FriendStatus.*;


@Service
public class FriendServiceImpl implements FriendService {

    FriendShipRepository friendShipRepository;
    FriendViewRepository friendViewRepository;
    UserRepository userRepository;

    public FriendServiceImpl(FriendShipRepository friendShipRepository,
                             FriendViewRepository friendViewRepository,
                             UserRepository userRepository) {
        this.friendShipRepository = friendShipRepository;
        this.friendViewRepository  = friendViewRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<FriendView> getFriendsByUsername(String username){

        Optional<List<FriendView>>  result =this.friendViewRepository.findAllFriendsVisual(username);
        if(result.isPresent()){
            return result.get();
        }

        return result.orElse(Collections.emptyList());
    }

    @Override
    public void sendInviteRequest(int userId, String username) {
        int friendId;
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent()){
            friendId = result.get().getId();
            this.friendShipRepository.sendingFriendRequest(userId, friendId);
            this.friendShipRepository.receiveTheRequest(friendId, userId);
        }
    }


    @Override
    @Transactional
    public void acceptFriend(int userId, String username) {
      List<FriendShip> friends = findFriends(userId);
      if(friends.isEmpty()) {
          return;
      }

        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent()) {
            int friendId = result.get().getId();
            for (FriendShip friend : friends) {
                if (friend.getStatus() == RESPONSE && friend.getFriend_id() == friendId) {
                    this.friendShipRepository.acceptFriendRequest(userId, friend.getFriend_id(), ACCEPTED);
                    this.friendShipRepository.acceptFriendRequest(friend.getFriend_id(), userId, ACCEPTED);
                    break;
                }
            }
        }
    }

    @Override
    public void declineFriend(int userId, String username) {
        List<FriendShip> friends = findFriends(userId);
        if(friends.isEmpty()) {
            return;
        }
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent()){
            int friendId = result.get().getId();
            for(FriendShip friend : friends){
                if(friend.getStatus().getText().equals("Response")
                        && friend.getFriend_id() == friendId){
                    this.friendShipRepository.deleteById(friend.getId());
                    this.friendShipRepository.deleteFriendRequest(friend.getFriend_id(), userId);
                    break;
                }
            }
        }
    }

    private List<FriendShip> findFriends(int userId){
            Optional<List<FriendShip>> friendsOptional = this.friendShipRepository.findByUserId(userId);
            if(friendsOptional.isPresent()){
                List<FriendShip> friends = friendsOptional.get();
            return friends;
            }
            return friendsOptional.orElse(Collections.emptyList());
    }

    @Override
    public void removeFriend(int userId, String username) {
        List<FriendShip> friends = this.findFriends(userId);
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent()) {
            int friendId = result.get().getId();
            for (FriendShip friend : friends) {
                if (friend.getFriend_id() == friendId) {
                    this.friendShipRepository.deleteById(friend.getId());
                }
            }
            friends = this.findFriends(friendId);
            for (FriendShip friend : friends) {
                if (friend.getFriend_id() == userId) {
                    this.friendShipRepository.deleteById(friend.getId());
                }
            }
        }
    }

    @Override
    public Set<UserStatusDTO> getUsernames(String userUsername) {
        Optional<List<String>> result = userRepository.findAllUsername();
        byte counter = 0;
        Random random = new Random();
        List<String> usernames;
        UserStatusDTO userStatus;
        Set<String> users = new HashSet<>();
        Set<UserStatusDTO> resultOfNames = new HashSet<>();
        if(result.isPresent()){
            usernames = result.get();
            Optional<List<FriendView>> friendsOptional = this.friendViewRepository.findAllFriendsVisual(userUsername);
            while(users.size() < 4 && counter < 6){
                counter++;
                String randomUsername = usernames.get(random.nextInt(usernames.size()));
                if(friendsOptional.isPresent()) {
                    List<FriendView> friendsOfUser = friendsOptional.get();
                    if(!this.checkIfFriendIsIncludedInFriendList(randomUsername, friendsOfUser)
                            && !userUsername.equals(randomUsername)){
                        users.add(randomUsername);
                    }
                }else{
                    if(!userUsername.equals(randomUsername)){
                        users.add(randomUsername);
                    }
                }
            }
            for(String username : users){
                userStatus = new UserStatusDTO(username, NOTHING.getText());
                resultOfNames.add(userStatus);
            }

            return resultOfNames;
        }
        return Collections.emptySet();
    }

    private boolean checkIfFriendIsIncludedInFriendList(String username, List<FriendView> friendNames){
            boolean isFriend = false;
        for(FriendView friend : friendNames){
            if(friend.getFriend().equals(username)){
                isFriend = true;
                break;
            }
        }
            return isFriend;
    }
}
