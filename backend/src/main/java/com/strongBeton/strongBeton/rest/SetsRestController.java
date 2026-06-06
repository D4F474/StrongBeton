package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.workout.SetsDTO;
import com.strongBeton.strongBeton.entity.workout.Sets;
import com.strongBeton.strongBeton.service.workout.SetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SetsRestController {

    private final SetsService setsService;

    @Autowired
    public SetsRestController(SetsService setsService) {
        this.setsService = setsService;
    }

    @GetMapping("/workout/sets/{workoutId}")
    public ResponseEntity<List<SetsDTO>> findSetsByWorkoutId(@PathVariable int workoutId){
        return ResponseEntity.ok(setsService.findSetsByWorkoutId(workoutId));
    }

    @PostMapping("/workout/newSet")
    public ResponseEntity<SetsDTO> newSet(@RequestBody Sets sets){
        SetsDTO set =  setsService.saveSet(sets);
        return ResponseEntity.ok(set);
    }

    @PutMapping("/workout/sets")
    public SetsDTO updateSets(@RequestBody Sets sets){
        return setsService.saveSet(sets);
    }

    @DeleteMapping("/deleteSet/{setId}")
    public void deleteNew(@PathVariable int setId){
        setsService.deleteSet(setId);
    }
}
