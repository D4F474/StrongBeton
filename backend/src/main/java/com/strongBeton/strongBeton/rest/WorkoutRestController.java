package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.*;
import com.strongBeton.strongBeton.entity.*;
import com.strongBeton.strongBeton.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WorkoutRestController {

    WorkoutService workoutService;


    @Autowired
    public WorkoutRestController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/workout/user/{userId}")
    public ResponseEntity<Map<String,List<WorkoutDTO>>> findByUserId(@PathVariable UUID userId){
        return  ResponseEntity.ok(workoutService.findByUserId(userId));
    }

    @GetMapping("/workout/workoutDetails/{workoutId}")
    public ResponseEntity<List<WorkoutDetailsDTO>> findWorkoutDetailsByWorkoutId(@PathVariable UUID workoutId,
                                                                                 @AuthenticationPrincipal User currUser){
        return ResponseEntity.ok(workoutService.findWorkoutDetailsById(workoutId,currUser));
    }

    @PostMapping("/workout/{userId}")
    public ResponseEntity<WorkoutDTO> newWorkout(@RequestBody WorkoutDTO workoutJSON, @PathVariable UUID userId,
                                                 @AuthenticationPrincipal User user){
        return ResponseEntity.ok(workoutService.save(workoutJSON, userId, user));
    }

    @PostMapping("/workout/{workoutId}/workoutDetails")
    public ResponseEntity<WorkoutDetailsDTO> newWorkoutDetails(@RequestBody WorkoutDetails workoutDetails,
                                                               @PathVariable UUID workoutId){
        workoutDetails.setWorkoutId(workoutId);
        return ResponseEntity.ok(workoutService.saveWorkoutDetails(workoutDetails));
    }

    @PutMapping("/workout/{userId}")
    public WorkoutDTO updateWorkout(@RequestBody WorkoutDTO workoutDTO, @PathVariable UUID userId, @AuthenticationPrincipal User user){
        return workoutService.save(workoutDTO, userId, user);
    }

    @PutMapping("/workout/{workoutId}/workoutDetails")
    public ResponseEntity<WorkoutDetailsDTO> updateWorkoutDetails(@RequestBody WorkoutDetails workoutDetails,
                                                                  @PathVariable UUID workoutId){
        workoutDetails.setWorkoutId(workoutId);
        WorkoutDetailsDTO workoutDetail = workoutService.saveWorkoutDetails(workoutDetails);
        return ResponseEntity.ok(workoutDetail);
    }

    @DeleteMapping("/deleteWorkoutDetail/{workoutDetailId}")
    public void deleteWorkoutDetail(@PathVariable int workoutDetailId){
        workoutService.deleteWorkoutDetailsById(workoutDetailId);
    }

    @DeleteMapping("/deleteWorkout/{workoutId}")
    public void deleteWorkout(@PathVariable UUID workoutId){
        workoutService.deleteWorkoutById(workoutId);
    }


}
