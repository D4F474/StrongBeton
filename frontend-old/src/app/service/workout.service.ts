import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Workout } from '../common/workout';
import { map, Observable } from 'rxjs';
import { WorkoutDetails } from '../common/workout-details';
import { Sets } from '../common/sets';
import { Exercise } from '../common/exercise';
import { UserDetails } from '../common/user-details';

@Injectable({
  providedIn: 'root'
})
export class WorkoutService {
 

  private baseUrl = 'http://localhost:8081/api';
  //private baseUrl = 'http://192.168.0.104:8081/api';
  
  constructor(private httpClient: HttpClient) { }

  getWorkouts(userId : string): Observable<Map<String,Workout[]>>{
    const Url = `${this.baseUrl}/workout/user/${userId}`;
      return this.httpClient.get<Map<String,Workout[]>>(Url);
  }

  saveWorkout(JSONFile : string, userId : string){
    const Url = `${this.baseUrl}/workout/${userId}`;
    return this.httpClient.post<Workout>(Url, JSONFile);    
  }

  getWorkoutDetails(workoutDetailsId: string): Observable<WorkoutDetails[]>{    
    const Url = `${this.baseUrl}/workout/workoutDetails/${workoutDetailsId}`
    return this.httpClient.get<WorkoutDetails[]>(Url);
  }
  
  getSets(exerciseId: number): Observable<Sets[]>{
    const Url = `${this.baseUrl}/workout/sets/${exerciseId}`;
    return this.httpClient.get<Sets[]>(Url);
  }
  
  getMuscleGroups(): Observable<string[]>{
    const Url = `${this.baseUrl}/muscleGroups`;
    return this.httpClient.get<string[]>(Url);
  }
  
  saveWorkoutDetail(workoutDetailJSON: WorkoutDetails, workoutId : string){
    const Url = `${this.baseUrl}/workout/${workoutId}/workoutDetails`;
    return this.httpClient.post<WorkoutDetails>(Url, workoutDetailJSON);
  }

  getSearchBarData(word:String, userId: string): Observable<Map<String,Workout[]>>{
    const Url = `${this.baseUrl}/search/${userId}/word/${word}`;
    return this.httpClient.get<Map<String,Workout[]>>(Url);
  }
  
  updateWorkoutDetail(workoutDetailJSON: Exercise, workoutId: string){
    const Url = `${this.baseUrl}/workout/${workoutId}/workoutDetails`;
    return this.httpClient.put<Exercise>(Url, workoutDetailJSON);
  }
  
  updateExerciseMuscle(workoutDetailJSON: WorkoutDetails) {
    const Url = `${this.baseUrl}/`;
    return this.httpClient.put<WorkoutDetails>(Url, workoutDetailJSON);
  }

  saveSet(setJson : Sets){
    const Url = `${this.baseUrl}/workout/newSet`
    
    return this.httpClient.post<Sets>(Url, setJson);
  }

  deleteSet(theId : number){
    const Url = `${this.baseUrl}/deleteSet/${theId}`;
    console.log(Url);
    
    return this.httpClient.delete(Url);
  }

  deleteExercise(theId : number){
      const Url = `${this.baseUrl}/deleteExercise/${theId}`;
      return this.httpClient.delete(Url);
  }

  deleteWorkoutDetails(theId : number){
    const Url = `${this.baseUrl}/deleteWorkoutDetail/${theId}`;

    return this.httpClient.delete(Url);
  }

  deleteWorkout(theId :string){
    const Url = `${this.baseUrl}/deleteWorkout/${theId}`;
    return this.httpClient.delete(Url);
  }

}

interface GetResponseWorkouts{
  _embedded:{
    workouts: Workout[];
  };
}
