package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.UserDTO;
import com.strongBeton.strongBeton.entity.FriendView;
import com.strongBeton.strongBeton.entity.User;
import com.strongBeton.strongBeton.service.FriendService;
import com.strongBeton.strongBeton.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@CrossOrigin
@RestController
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    @Autowired
    public UserController(UserService userService,
                          FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> authenticatedUser() {
        UserDTO userDTO = userService.loadUserDataByUsername(getCurrentUser().getUsername());
        return ResponseEntity.ok(userDTO);
    }


    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/seeAllUsers")
    public ResponseEntity<List<UserDTO>>
    @GetMapping("/seeAllFriends/{username}")
    public ResponseEntity<List<FriendView>> findAllFriends(@PathVariable("username") String username){
       return ResponseEntity.ok(this.friendService.getFriendsByUsername(username));
    }

    @PostMapping("acceptFriendRequest/{friendId}")
    public void acceptFriendRequest(@PathVariable("friendId") int friendId){
        this.friendService.acceptFriend(getCurrentUser(), friendId);
    }

    @PostMapping("inviteFriendRequest/{friendId}")
    public void inviteFriendRequest(@PathVariable("friendId") int friendId){
        this.friendService.sendInviteRequest(getCurrentUser(), friendId);
    }

    @DeleteMapping("declineFriendRequest/{friendId}")
    public void declineFriendRequest(@PathVariable("friendId") int friendId){
        this.friendService.declineFriend(getCurrentUser(), friendId);
    }

    @DeleteMapping("removeFriend/{friendId}")
    public void removeFriendRequest(@PathVariable("friendId") int friendId){
        this.friendService.removeFriend(getCurrentUser(), friendId);
    }
}
