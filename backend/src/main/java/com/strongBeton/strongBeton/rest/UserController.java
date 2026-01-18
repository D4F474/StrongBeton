package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.UserDTO;
import com.strongBeton.strongBeton.DTO.UserStatusDTO;
import com.strongBeton.strongBeton.entity.FriendView;
import com.strongBeton.strongBeton.entity.User;
import com.strongBeton.strongBeton.service.FriendService;
import com.strongBeton.strongBeton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    public  User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser;
    }

    @GetMapping("/me")
    public UserDTO getUser(){
        return this.userService.loadUserDataByUsername(getCurrentUser().getUsername());
    }


    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/GetUser/{username}")
    ResponseEntity<UserDTO> findDataForUser(@PathVariable("username") String username){
        return ResponseEntity.ok(this.userService.loadUserDataByUsername(username));
    }

    @GetMapping("/ListAllUsernames")
    public ResponseEntity<Set<UserStatusDTO>> findAllUsernames(){
        return ResponseEntity.ok(this.friendService.getUsernames(this.getCurrentUser().getUsername()));
    }

    @GetMapping("/seeAllFriends/{username}")
    public ResponseEntity<List<FriendView>> findAllFriends(@PathVariable("username") String username){
       return ResponseEntity.ok(this.friendService.getFriendsByUsername(username));
    }

    @PostMapping("/acceptFriendRequest/{username}")
    public void acceptFriendRequest(@PathVariable("username") String username){
        this.friendService.acceptFriend(getCurrentUser().getId(), username);
    }

    @PostMapping("/inviteFriendRequest/{username}")
    public void inviteFriendRequest(@PathVariable("username") String username){
        this.friendService.sendInviteRequest(getCurrentUser().getId(), username);
    }

    @DeleteMapping("/declineFriendRequest/{username}")
    public void declineFriendRequest(@PathVariable("username") String username){
        this.friendService.declineFriend(getCurrentUser().getId(), username);
    }

    @DeleteMapping("/removeFriend/{username}")
    public void removeFriendRequest(@PathVariable("username") String username){
        this.friendService.removeFriend(getCurrentUser().getId(), username);
    }
}
