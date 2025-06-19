import { Component, OnInit } from '@angular/core';
import { WorkoutService } from '../../service/workout.service';
import { ActivatedRoute } from '@angular/router';
import { WorkoutDetails } from '../../common/workout-details';
import { Sets } from '../../common/sets';
import { Workout } from '../../common/workout';
import { count, forkJoin } from 'rxjs';
import { Form, FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { KeyValue } from '@angular/common';
import { Exercise } from '../../common/exercise';

@Component({
  selector: 'app-workout-details',
  standalone: false,
  
  templateUrl: './workout-details.component.html',
  styleUrl: './workout-details.component.css'
})

export class WorkoutDetailsComponent implements OnInit {
  
  workoutDetails: WorkoutDetails[] = [];
  workouts = new Map<WorkoutDetails, Sets[]>();
  sets: Sets[] = [];
  sumOfWeight :number = 0;
  muscleGroups: string[] = [];  
  workoutId: number =0;

  constructor(private workoutService: WorkoutService,
    private route: ActivatedRoute 
  ){
    
  }
  
  ngOnInit(): void {
    this.route.paramMap.subscribe(()=>{
      this.handleWorkouts();
    })
    
    this.route.paramMap.subscribe(params => {
      this.workoutId = Number(params.get('id'))});
      console.log('Workout ID:', this.workoutId);

  }

  loadData(){
    const theWorkoutDetailsId: number = +this.route.snapshot.paramMap.get('id')!;
    
    forkJoin({
      workoutDetails: this.workoutService.getWorkoutDetails(theWorkoutDetailsId)
    }).subscribe(results =>{
      this.workoutDetails = results.workoutDetails;
      
      for(let workout of this.workoutDetails){
        this.workoutService.getSets(workout.id).subscribe(data=>{
          this.sets.push(...data);
          this.groupSetsByWorkoutDetails();
        });
      }
         
    })
      this.workoutService.getMuscleGroups().subscribe(
        data => {
          this.muscleGroups = data;
        }
      );
  }

  handleWorkouts(){
    this.loadData();
    
  }
  
  groupSetsByWorkoutDetails() {
    this.workouts.clear();
    for (let workout of this.workoutDetails) {
      const setsForWorkout = this.sets.filter(set => set.workoutDetailsId === workout.id);
      this.workouts.set(workout, setsForWorkout);
    }
    console.log(this.workouts.values());
  }
  
  sumOfWeights(sets: Sets[]): number{
    let sum =0;
    for(let set of sets) {
      sum += set.kg * set.reps;
    }
    return sum;
    
  }

  addNewExercise() {
    let newWorkout = new WorkoutDetails(0, new Exercise(0, ''), '');

  this.workoutService.saveWorkoutDetail(newWorkout, this.workoutId).subscribe(workout => {
    newWorkout.id = workout.id; 

    this.workouts.set(newWorkout, []);

    let newSet = new Sets(0, 0, 0, 1, newWorkout.id);

    this.workoutService.saveSet(newSet).subscribe(set => {
      newSet.id = set.id;
      
      this.workouts.get(newWorkout)?.push(newSet);
    });
  });

  }
  
  addNewSet(workoutId: number){
    for(let tempWorkout of this.workouts.keys()){
      if(tempWorkout.id === workoutId){
        let setNum = Number(this.workouts.get(tempWorkout)?.length);
        let newSet = new Sets(0,0,0,++setNum,workoutId);
        const setJSON: Sets = JSON.parse(JSON.stringify(newSet));
        console.log(setJSON);
        this.workoutService.saveSet(setJSON).subscribe(
          set => newSet.id = set.id
          );
          this.workouts.get(tempWorkout)?.push(newSet);          
      break;
      }
    }
    console.log(this.workouts.values());
  }
  
  removeExercise(workoutDetail: WorkoutDetails) {
    
    for(let tempWorkout of this.workouts.keys()){
      if(tempWorkout.id === workoutDetail.id){
        this.deleteSets(tempWorkout);
        this.workoutService.deleteWorkoutDetails(tempWorkout.id).subscribe();
        this.workouts.delete(tempWorkout);
      }
    }
  }

  deleteSets(workoutDetail: WorkoutDetails){
    this.workouts.get(workoutDetail)?.forEach(
      set =>{
        this.workoutService.deleteSet(set.id).subscribe();
      }
    )
  }

  removeSet(workoutDetail: WorkoutDetails) {
    let num : number =0;
    num = Number(this.workouts.get(workoutDetail)?.pop()?.id);
    console.log(num);
    
    this.workoutService.deleteSet(num).subscribe();
    if(Number(this.workouts.get(workoutDetail)?.length) < 1){
      this.removeExercise(workoutDetail);
    }
  }

  addKgOrReps(workoutDetail: WorkoutDetails, editSet: Sets){
    this.workouts.get(workoutDetail)?.forEach(set => {
      if(set.id == editSet.id){
        set = editSet;
        const setJSON = JSON.parse(JSON.stringify(set));
        this.workoutService.saveSet(setJSON).subscribe();
      }
    });
  }

  updateWorkoutDetail(workoutDetail : WorkoutDetails){
    workoutDetail.exercise.id = 0;
    const JSONWorkoutDetail = JSON.parse(JSON.stringify(workoutDetail));
    console.log(JSONWorkoutDetail);
    
    this.workoutService.updateWorkoutDetail(JSONWorkoutDetail, this.workoutId).subscribe();
  }

  /*updateExerciseName(exercise: Exercise){
    for(let tempWorkout of this.workouts.keys()){
      if(tempWorkout.exercise.name === exercise.name){
        console.log(`Exercise: ${exercise.name}
          \nTempExercise ${tempWorkout.exercise.name}`);
        tempWorkout.exercise.name = exercise.name;
        const workoutJSON = JSON.parse(JSON.stringify(tempWorkout.exercise));
        console.log(workoutJSON);
        this.workoutService.updateWorkoutDetail(workoutJSON).subscribe(
          exercise => tempWorkout.exercise.name = exercise.name
        );
        console.log("Components");
        
      }
    }
  }
*/

  changeMuscleGroup(event: Event, workoutDetail: WorkoutDetails) {
    
    let muscleName : string = (event.target as HTMLInputElement).value;
    workoutDetail.muscleGroup = muscleName;
    //workoutDetail.exercise.name = ;
    const JSONWorkoutDetail = JSON.parse(JSON.stringify(workoutDetail))
    this.workoutService.updateWorkoutDetail(JSONWorkoutDetail, this.workoutId).subscribe();
    
  }
}
