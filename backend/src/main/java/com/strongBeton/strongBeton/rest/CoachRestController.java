package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.CoachService;
import com.strongBeton.strongBeton.service.workout.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CoachRestController {

    CoachService coachService;
    WorkoutService  workoutService;

    @Autowired
    public CoachRestController(CoachService coachService, WorkoutService workoutService) {
        this.coachService = coachService;
        this.workoutService = workoutService;
    }

    @PostMapping("/AccUpToCoach")
    public ResponseEntity<String> promoteAccountToCoach(@AuthenticationPrincipal User currentUser){
        if(this.coachService.promoteToCoachAccount(currentUser)){
            return ResponseEntity.ok("Updated!");
        }
        return ResponseEntity.ok("Something went wrong!");
    }

    @PostMapping("/inviteToBeCoach/{coachId}")
    public ResponseEntity<String> inviteToBeCoach(@AuthenticationPrincipal User currentUser, @PathVariable int coachId){
        if(this.coachService.inviteToBeACoach(currentUser, coachId)){
            return ResponseEntity.ok("Invited to be your coach!");
        }
        return ResponseEntity.ok("Something went wrong!");
    }

    @PostMapping("/acceptCoach/{clientId}")
    public ResponseEntity<String> acceptCoach(@AuthenticationPrincipal User currentUser, @PathVariable int clientId){
        if(this.coachService.acceptToBeACoach(currentUser, clientId)){
            return ResponseEntity.ok("Accepted he is your coach!");
        }
        return ResponseEntity.ok("Something went wrong!");
    }

    @PostMapping("/fireCoach/{coachId}")
    public ResponseEntity<String> fireCoach(@AuthenticationPrincipal User currentUser, @PathVariable int coachId){
        if(this.coachService.fireACoach(currentUser,coachId)){
            return ResponseEntity.ok("Fired he is not anymore your coach!");
        }
        return ResponseEntity.ok("Something went wrong!");
    }

    @GetMapping("/getUserWorkout/{uuidClient}")
    public ResponseEntity<?> getUserWorkout(@AuthenticationPrincipal User currentUser, @PathVariable UUID uuidClient){
        if (this.coachService.checkWorkoutFromCoach(currentUser, uuidClient)) {
            return ResponseEntity.ok(workoutService.findByUserId(uuidClient));

        } else {
            return ResponseEntity.ok("Something went wrong!");
        }
    }

    //Вземаме детайлите от WorkoutDetails трябва да направя защита да могат само COACH и собственик да пипат

}
