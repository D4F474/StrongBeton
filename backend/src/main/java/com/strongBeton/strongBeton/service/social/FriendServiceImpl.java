package com.strongBeton.strongBeton.service.social;

import com.strongBeton.strongBeton.dto.social.FriendShipDTO;
import com.strongBeton.strongBeton.dto.user.FriendViewDTO;
import com.strongBeton.strongBeton.dto.user.ImageDataDTO;
import com.strongBeton.strongBeton.dto.user.UserStatusDTO;
import com.strongBeton.strongBeton.dao.FriendShipRepository;
import com.strongBeton.strongBeton.dao.FriendViewRepository;
import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.entity.UserSuggestionProjection;
import com.strongBeton.strongBeton.entity.social.FriendShip;
import com.strongBeton.strongBeton.entity.social.FriendView;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.strongBeton.strongBeton.enums.FriendStatus.*;


@Service
public class FriendServiceImpl implements FriendService {

    FriendShipRepository friendShipRepository;
    FriendViewRepository friendViewRepository;
    UserRepository userRepository;
    ImageService imageService;
    public FriendServiceImpl(FriendShipRepository friendShipRepository,
                             FriendViewRepository friendViewRepository,
                             UserRepository userRepository,
                             ImageService imageService) {
        this.friendShipRepository = friendShipRepository;
        this.friendViewRepository  = friendViewRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    @Transactional
    public List<FriendViewDTO> getFriendsByUsername(String username) {
        if (username == null || username.isBlank()) {
            return Collections.emptyList();
        }

        List<FriendView> friends = this.friendViewRepository
                .findAllFriendsVisual(username)
                .orElse(Collections.emptyList());

        List<FriendViewDTO> result = new ArrayList<>();

        for (FriendView friend : friends) {
            String profileImageUrl = null;

            Optional<User> userOptional = this.userRepository.findByUsername(friend.getFriend());

            if (userOptional.isPresent()) {
                int friendId = userOptional.get().getId();

                Optional<ImageDataDTO> imageOptional = this.imageService.getProfileImage(friendId);

                if (imageOptional.isPresent()) {
                    profileImageUrl = imageOptional.get().getPhotoUrl();
                }
            }

            FriendViewDTO friendViewDTO = new FriendViewDTO(
                    friend.getFriend(),
                    friend.getStatus(),
                    profileImageUrl
            );

            result.add(friendViewDTO);
        }

        return result;
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
      List<FriendShipDTO> friends = findFriends(userId);
      if(friends.isEmpty()) {
          return;
      }

        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent()) {
            int friendId = result.get().getId();
            for (FriendShipDTO friend : friends) {
                if (friend.getStatus() == RESPONSE && friend.getFriend_id() == friendId) {
                    this.friendShipRepository.acceptFriendRequest(userId, friend.getFriend_id());
                    this.friendShipRepository.acceptFriendRequest(friend.getFriend_id(), userId);
                    break;
                }
            }
        }
    }

    @Override
    public void declineFriend(int userId, String username) {
        List<FriendShipDTO> friends = findFriends(userId);
        if(friends.isEmpty()) {
            return;
        }
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent()){
            int friendId = result.get().getId();
            for(FriendShipDTO friend : friends){
                if(friend.getStatus().getText().equals("Response")
                        && friend.getFriend_id() == friendId){
                    this.friendShipRepository.deleteById(friend.getId());
                    this.friendShipRepository.deleteFriendRequest(friend.getFriend_id(), userId);
                    break;
                }
            }
        }
    }

    private List<FriendShipDTO> findFriends(int userId){
        Optional<List<FriendShip>> friendsOptional = this.friendShipRepository.findByUserId(userId);
        List<FriendShipDTO> friendShipDTOS = new ArrayList<>();

        if(friendsOptional.isPresent()){
                List<FriendShip> friends = friendsOptional.get();
                for (FriendShip friendShip : friends) {
                    Optional<ImageDataDTO> userPhoto = imageService.getProfileImage(friendShip.getFriend_id());
                    FriendShipDTO friendShipDTO;
                    if(userPhoto.isPresent()){
                        friendShipDTO = new FriendShipDTO(friendShip.getId(), friendShip.getUser_id(),
                                friendShip.getFriend_id(), friendShip.getStatus(), userPhoto.get().getPhotoUrl());
                    }else{
                        friendShipDTO = new FriendShipDTO(friendShip.getId(), friendShip.getUser_id(),
                                friendShip.getFriend_id(), friendShip.getStatus());
                    }

                    friendShipDTOS.add(friendShipDTO);
                }
            return friendShipDTOS;
            }
            return friendShipDTOS;
    }

    @Override
    public void removeFriend(int userId, String username) {
        List<FriendShipDTO> friends = this.findFriends(userId);
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent()) {
            int friendId = result.get().getId();
            for (FriendShipDTO friend : friends) {
                if (friend.getFriend_id() == friendId) {
                    this.friendShipRepository.deleteById(friend.getId());
                }
            }
            friends = this.findFriends(friendId);
            for (FriendShipDTO friend : friends) {
                if (friend.getFriend_id() == userId) {
                    this.friendShipRepository.deleteById(friend.getId());
                }
            }
        }
    }

    @Override
    @Transactional
    public Set<UserStatusDTO> getUsernames(String userUsername) {
        List<UserSuggestionProjection> allUsers =
                this.userRepository.findAllUsersForSuggestions();

        if (allUsers == null || allUsers.isEmpty()) {
            return Collections.emptySet();
        }

        List<FriendView> friendsOfUser = this.friendViewRepository
                .findAllFriendsVisual(userUsername)
                .orElse(Collections.emptyList());

        Set<String> unavailableUsers = new HashSet<>();
        unavailableUsers.add(userUsername);

        for (FriendView friendView : friendsOfUser) {
            unavailableUsers.add(friendView.getFriend());
        }

        List<UserSuggestionProjection> availableUsers = allUsers
                .stream()
                .filter(user -> user.getUsername() != null)
                .filter(user -> !unavailableUsers.contains(user.getUsername()))
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(availableUsers);

        return availableUsers
                .stream()
                .limit(4)
                .map(user -> new UserStatusDTO(
                        user.getUsername(),
                        NOTHING.getText(),
                        user.getProfileImageUrl()
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
