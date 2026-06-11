package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.workout.ExerciseDTO;
import com.strongBeton.strongBeton.entity.workout.Exercise;
import com.strongBeton.strongBeton.service.workout.ExercisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ExercisesRestController {

    ExercisesService exercisesService;

    @Autowired
    public ExercisesRestController(ExercisesService exercisesService) {
        this.exercisesService = exercisesService;
    }

    @GetMapping("/exercises")
    public Set<ExerciseDTO> findAllExercises(){
        return exercisesService.findAllExercises();
    }

    @GetMapping("/muscleGroups")
    public Set<String> findAllMuscleGroups(){
        return exercisesService.findAllMuscleGroups();
    }

    @PostMapping("/newExercise")
    public ExerciseDTO newExercise(Exercise exercise){
        return exercisesService.save(exercise);
    }

    @PutMapping("/newExercise")
    public ExerciseDTO updateExercise(Exercise exercise){
        return exercisesService.save(exercise);
    }

    @DeleteMapping("/deleteExercise")
    public void deleteExercise(int theId){
        exercisesService.deleteById(theId);
    }
}
