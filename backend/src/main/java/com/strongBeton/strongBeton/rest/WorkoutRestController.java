package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.workout.ActiveWorkoutPreviewDTO;
import com.strongBeton.strongBeton.dto.workout.WorkoutDTO;
import com.strongBeton.strongBeton.dto.workout.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.entity.workout.WorkoutDetails;
import com.strongBeton.strongBeton.service.workout.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @GetMapping("/workout/active")
    public ResponseEntity<ActiveWorkoutPreviewDTO> getActiveWorkout(
            @AuthenticationPrincipal User user
    ) {


        return ResponseEntity.ok(workoutService.findActiveWorkoutPreview(user).orElseThrow());

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

    @PostMapping("/{workoutId}/finish")
    public ResponseEntity<WorkoutDTO> finishWorkout(
            @PathVariable UUID workoutId,
            @AuthenticationPrincipal User user
    )
    {
        return ResponseEntity.ok(workoutService.finishWorkout(workoutId, user));
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
