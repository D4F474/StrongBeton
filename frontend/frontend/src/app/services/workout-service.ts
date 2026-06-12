import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';

import {
  ExerciseDto,
  SetDto,
  WorkoutDetailsDto,
  WorkoutDto,
  WorkoutGroups,
} from '../common/workout';

export type ActiveWorkoutPreview = {
  id: string;
  name: string;
  currentExercise?: string | null;
};

@Injectable({
  providedIn: 'root',
})
export class WorkoutService {
  private readonly baseUrl = '/api';

  constructor(private httpClient: HttpClient) {}

  getExercises(): Observable<ExerciseDto[]> {
    return this.httpClient.get<ExerciseDto[]>(`${this.baseUrl}/exercises`);
  }

  getUserWorkouts(userId: string): Observable<WorkoutGroups> {
    return this.httpClient.get<WorkoutGroups>(
      `${this.baseUrl}/workout/user/${userId}`
    );
  }

  createWorkout(userId: string, workoutName: string): Observable<WorkoutDto> {
    return this.httpClient.post<WorkoutDto>(
      `${this.baseUrl}/workout/${userId}`,
      { workoutName }
    );
  }

  getWorkoutDetails(workoutId: string): Observable<WorkoutDetailsDto[]> {
    return this.httpClient.get<WorkoutDetailsDto[]>(
      `${this.baseUrl}/workout/workoutDetails/${workoutId}`
    );
  }

  addWorkoutDetail(
    workoutId: string,
    exerciseId: number
  ): Observable<WorkoutDetailsDto> {
    return this.httpClient.post<WorkoutDetailsDto>(
      `${this.baseUrl}/workout/${workoutId}/workoutDetails`,
      {
        exercise: {
          id: exerciseId,
        },
      }
    );
  }

  getSets(workoutDetailsId: number): Observable<SetDto[]> {
    return this.httpClient.get<SetDto[]>(
      `${this.baseUrl}/workout/sets/${workoutDetailsId}`
    );
  }

  saveSet(set: SetDto): Observable<SetDto> {
    return this.httpClient.post<SetDto>(
      `${this.baseUrl}/workout/newSet`,
      set
    );
  }

  updateSet(set: SetDto): Observable<SetDto> {
    return this.httpClient.put<SetDto>(`${this.baseUrl}/workout/sets`, set);
  }

  deleteSet(setId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/deleteSet/${setId}`);
  }

  deleteWorkoutDetail(workoutDetailId: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.baseUrl}/deleteWorkoutDetail/${workoutDetailId}`
    );
  }

  finishWorkout(workoutId: string): Observable<WorkoutDto> {
    return this.httpClient.post<WorkoutDto>(
      `${this.baseUrl}/${workoutId}/finish`,
      {}
    );
  }

  getActiveWorkoutPreview(): Observable<ActiveWorkoutPreview | null> {
    return this.httpClient
      .get<ActiveWorkoutPreview | null>(`${this.baseUrl}/workout/active`)
      .pipe(
        catchError((error) => {
          console.error('ACTIVE WORKOUT ERROR:', error);
          return of(null);
        })
      );
  }
}
