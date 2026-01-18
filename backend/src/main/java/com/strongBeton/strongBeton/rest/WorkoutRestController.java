package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.*;
import com.strongBeton.strongBeton.entity.*;
import com.strongBeton.strongBeton.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/search/{userId}/word/{keyword}")
    public ResponseEntity<List<WorkoutDTO>> findBySearchbar(@PathVariable("userId") UUID userId, @PathVariable("keyword") String keyword){
        return ResponseEntity.ok(workoutService.findBySearchbar(userId, keyword));
    }

    @PostMapping("/workout/{userId}")
    public ResponseEntity<WorkoutDTO> newWorkout(@RequestBody WorkoutDTO workoutJSON, @PathVariable UUID userId){
        return ResponseEntity.ok(workoutService.save(workoutJSON, userId));
    }

    @PutMapping("/workout/{userId}")
    public WorkoutDTO updateWorkout(@RequestBody WorkoutDTO workoutDTO, @PathVariable UUID userId){
        return workoutService.save(workoutDTO, userId);
    }

    @DeleteMapping("/deleteWorkout/{workoutId}")
    public void deleteWorkout(@PathVariable UUID workoutId){
        workoutService.deleteWorkoutById(workoutId);
    }


}
