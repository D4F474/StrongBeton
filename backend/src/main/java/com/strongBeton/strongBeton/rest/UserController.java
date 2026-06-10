package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.user.UserUpdateDTO;
import com.strongBeton.strongBeton.dto.workout.InjuriesDTO;
import com.strongBeton.strongBeton.dto.user.UserDTO;
import com.strongBeton.strongBeton.dto.user.UserStatusDTO;
import com.strongBeton.strongBeton.entity.social.FriendView;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.social.FriendService;
import com.strongBeton.strongBeton.service.workout.InjuriesService;
import com.strongBeton.strongBeton.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/users")
@CrossOrigin
@RestController
public class UserController {

    private final UserService userService;
    private final FriendService friendService;
    private final InjuriesService injuriesService;

    @Autowired
    public UserController(UserService userService,
                          FriendService friendService,
                          InjuriesService injuriesService) {
        this.userService = userService;
        this.friendService = friendService;
        this.injuriesService = injuriesService;
    }


    @GetMapping("/me")
    public UserDTO getUser(@AuthenticationPrincipal User currentUser) {
        return this.userService.loadUserDataByEmail(currentUser.getEmail());
    }


    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/GetUser")
    ResponseEntity<UserDTO> findDataForUser(@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(this.userService.loadUserDataByEmail(currentUser.getEmail()));
    }

    @GetMapping("/ListAllUsernames")
    public ResponseEntity<?> findAllUsernames(@AuthenticationPrincipal User currentUser) {
        Set<UserStatusDTO> usernames = this.friendService.getUsernames(currentUser.getUsername());
        if(!usernames.isEmpty()) {
            return ResponseEntity.ok(usernames);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no people to recommend.");
    }

    @GetMapping("/seeAllFriends/{username}")
    public ResponseEntity<?> findAllFriends(@PathVariable("username") String username) {
        List<FriendView> friends = this.friendService.getFriendsByUsername(username);
        if(!friends.isEmpty()){
            return ResponseEntity.ok(friends);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There's no friends");
    }

    @GetMapping("/Injuries")
    public ResponseEntity<?> getInjuries(@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(this.injuriesService.getInjuriesForCurrentUser(currentUser));
    }

    @PostMapping("/acceptFriendRequest/{username}")
    public void acceptFriendRequest(@PathVariable("username") String username,
                                    @AuthenticationPrincipal User currentUser){
        this.friendService.acceptFriend(currentUser.getId(), username);
    }

    @PostMapping("/inviteFriendRequest/{username}")
    public void inviteFriendRequest(@PathVariable("username") String username,
                                    @AuthenticationPrincipal User currentUser){
        this.friendService.sendInviteRequest(currentUser.getId(), username);
    }

    @PostMapping("/AddNewInjury")
    public ResponseEntity<?> addInjury(@AuthenticationPrincipal User currentUser, @RequestBody InjuriesDTO injuriesDTO){
        return ResponseEntity.ok(this.injuriesService.addInjuryForCurrentUser(currentUser,injuriesDTO));
    }

    @PutMapping("/updateUserData")
    public ResponseEntity<?> updateUserData(@AuthenticationPrincipal User currentUser,
                                            @RequestBody UserDTO userUpdateDTO){
        return ResponseEntity.ok(this.userService.updateUser(currentUser.getUuid(), userUpdateDTO));
    }

    @DeleteMapping("/declineFriendRequest/{username}")
    public void declineFriendRequest(@PathVariable("username") String username,
                                     @AuthenticationPrincipal User currentUser){
        this.friendService.declineFriend(currentUser.getId(), username);
    }

    @DeleteMapping("/removeFriend/{username}")
    public void removeFriendRequest(@PathVariable("username") String username,
                                    @AuthenticationPrincipal User currentUser){
        this.friendService.removeFriend(currentUser.getId(), username);
    }


}
