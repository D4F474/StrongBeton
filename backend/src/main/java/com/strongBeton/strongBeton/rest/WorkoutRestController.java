package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.*;
import com.strongBeton.strongBeton.entity.*;
import com.strongBeton.strongBeton.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WorkoutRestController {

    WorkoutService workoutService;

    @Autowired
    public WorkoutRestController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/leaderBoard")
    public ResponseEntity<List<LeaderBoard>> findLeaderBoard(){
        return ResponseEntity.ok(workoutService.findLeaderBoardForUser());
    }
    @GetMapping("/workouts")
    public ResponseEntity<List<WorkoutDTO>> findAll(){
        return ResponseEntity.ok(workoutService.findAll());
    }

    @GetMapping("/workout/user/{userId}")
    public ResponseEntity<List<WorkoutDTO>> findByUserId(@PathVariable int userId){
        return  ResponseEntity.ok(workoutService.findByUserId(userId));
    }

    @GetMapping("/workout/workoutDetails/{workoutId}")
    public ResponseEntity<List<WorkoutDetailsDTO>> findWorkoutDetailsByWorkoutId(@PathVariable int workoutId){
        return ResponseEntity.ok(workoutService.findWorkoutDetailsById(workoutId));
    }

    @GetMapping("/workout/{workoutId}")
    public WorkoutDTO findById(@PathVariable int workoutId){
        return workoutService.findById(workoutId);
    }

    @GetMapping("/workout/sets/{workoutId}")
    public ResponseEntity<List<SetsDTO>> findSetsByWorkoutId(@PathVariable int workoutId){
        return ResponseEntity.ok(workoutService.findSetsByWorkoutId(workoutId));
    }

    @GetMapping("/search/{userId}/word/{keyword}")
    public ResponseEntity<List<WorkoutDTO>> findBySearchbar(@PathVariable("userId") int userId, @PathVariable("keyword") String keyword){
        System.out.println("Rest"+ "userId" + userId + " " + keyword );
        return ResponseEntity.ok(workoutService.findBySearchbar(userId, keyword));
    }

    @PostMapping("/workout/{userId}")
    public ResponseEntity<WorkoutDTO> newWorkout(@RequestBody Workout workoutJSON, @PathVariable int userId){
        System.out.println(workoutJSON);
        workoutJSON.setUser(workoutService.findUserByFromUserId(userId));
        WorkoutDTO workout = workoutService.save(workoutJSON);
        return ResponseEntity.ok(workout);
    }

    @PostMapping("/workout/newSet")
    public ResponseEntity<SetsDTO> newSet(@RequestBody Sets sets){
        SetsDTO set =  workoutService.saveSet(sets);
        return ResponseEntity.ok(set);
    }

    @PostMapping("/workout/{workoutId}/workoutDetails")
    public ResponseEntity<WorkoutDetailsDTO> newWorkoutDetails(@RequestBody WorkoutDetails workoutDetails,
                                                               @PathVariable int workoutId){
        workoutDetails.setWorkoutId(workoutId);
        WorkoutDetailsDTO workoutDetail = workoutService.saveWorkoutDetails(workoutDetails);
        return ResponseEntity.ok(workoutDetail);
    }

    @PutMapping("/workout")
    public WorkoutDTO updateWorkout(@RequestBody Workout workout){
        return workoutService.save(workout);
    }

    

    @PutMapping("/workout/sets")
    public SetsDTO updateSets(@RequestBody Sets sets){
        return workoutService.saveSet(sets);
    }

    @PutMapping("/workout/{workoutId}/workoutDetails")
    public ResponseEntity<WorkoutDetailsDTO> updateWorkoutDetails(@RequestBody WorkoutDetails workoutDetails,
                                                               @PathVariable int workoutId){
        System.out.println("Rest: " + workoutDetails);
        workoutDetails.setWorkoutId(workoutId);
        WorkoutDetailsDTO workoutDetail = workoutService.saveWorkoutDetails(workoutDetails);
        return ResponseEntity.ok(workoutDetail);
    }

    @DeleteMapping("/deleteWorkoutDetail/{workoutDetailId}")
    public void deleteWorkoutDetail(@PathVariable int workoutDetailId){
        workoutService.deleteWorkoutDetailsById(workoutDetailId);
    }

    @DeleteMapping("/deleteWorkout/{workoutId}")
    public void deleteWorkout(@PathVariable int workoutId){

        List<WorkoutDetailsDTO> workoutDetailsDTO = workoutService.findWorkoutDetailsById(workoutId);
        if (!workoutDetailsDTO.isEmpty()){
            for (WorkoutDetailsDTO detailsDTO : workoutDetailsDTO) {
                List<SetsDTO> sets = workoutService.findSetsByWorkoutId(detailsDTO.getId());
                for (SetsDTO set : sets) {
                    workoutService.deleteSet(set.getId());
                }
                workoutService.deleteWorkoutDetailsById(detailsDTO.getId());
            }
        }
        workoutService.deleteWorkoutById(workoutId);
    }

    @DeleteMapping("/deleteSet/{setId}")
    public void deleteNew(@PathVariable int setId){
        workoutService.deleteSet(setId);
    }
}
