package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.WorkoutDetailsDTO;
import com.strongBeton.strongBeton.entity.WorkoutDetails;
import com.strongBeton.strongBeton.service.WorkoutDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WorkoutDetailsRestController {

    private final WorkoutDetailsService workoutDetailsService;

    @Autowired
    public WorkoutDetailsRestController(WorkoutDetailsService workoutDetailsService) {
        this.workoutDetailsService  = workoutDetailsService;
    }

    @GetMapping("/workout/workoutDetails/{workoutId}")
    public ResponseEntity<List<WorkoutDetailsDTO>> findWorkoutDetailsByWorkoutId(@PathVariable int workoutId){
        return ResponseEntity.ok(workoutDetailsService.findWorkoutDetailsById(workoutId));
    }

    @PostMapping("/workout/{workoutId}/workoutDetails")
    public ResponseEntity<WorkoutDetailsDTO> newWorkoutDetails(@RequestBody WorkoutDetails workoutDetails,
                                                               @PathVariable int workoutId){
        workoutDetails.setWorkoutId(workoutId);
        return ResponseEntity.ok(workoutDetailsService.saveWorkoutDetails(workoutDetails));
    }

    @PutMapping("/workout/{workoutId}/workoutDetails")
    public ResponseEntity<WorkoutDetailsDTO> updateWorkoutDetails(@RequestBody WorkoutDetails workoutDetails,
                                                                  @PathVariable int workoutId){
        workoutDetails.setWorkoutId(workoutId);
        WorkoutDetailsDTO workoutDetail = workoutDetailsService.saveWorkoutDetails(workoutDetails);
        return ResponseEntity.ok(workoutDetail);
    }

    @DeleteMapping("/deleteWorkoutDetail/{workoutDetailId}")
    public void deleteWorkoutDetail(@PathVariable int workoutDetailId){
        workoutDetailsService.deleteWorkoutDetailsById(workoutDetailId);
    }
}
