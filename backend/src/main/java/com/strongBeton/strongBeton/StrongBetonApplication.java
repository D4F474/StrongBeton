package com.strongBeton.strongBeton;

import com.strongBeton.strongBeton.service.workout.ExercisesService;
import com.strongBeton.strongBeton.service.workout.WorkoutService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class StrongBetonApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrongBetonApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ExercisesService exercisesService, WorkoutService workoutService) {

		return runner -> {
			//System.out.println(exercisesDAO.findExerciseByName("Bench"));
			//System.out.println(workoutDetailsDAO.findAll());
			//System.out.println(userDAO.findAll());
			//System.out.println(exerciseService.findAll());
			//System.out.println(cityDAO.findAll());
			//System.out.println(addressDAO.findAll());
			//System.out.println(additionalInfoDAO.findAll());
			//System.out.println(exercisesDAO.findAll());
			//System.out.println(muscleGroupDAO.findAll());
			//System.out.println(exerciseService.findAll());
			//System.out.println(userService.findAll());
			//System.out.println(workoutRestController.findSetsByWorkoutId(1));
			//System.out.println(workoutService.findWorkoutDetailsById(3).get(0).getExercise().getMuscleGroup());
			//System.out.println(exercisesService.save(new Exercise("Klekame" )));
			//System.out.println(workoutService.findWorkoutDetailsById(2));
			//System.out.println(workoutService.findWorkoutDetailById(2));
		};
	}

}
