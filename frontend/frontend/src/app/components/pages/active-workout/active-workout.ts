import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import {
  ExerciseDto,
  SetDto,
  WorkoutDetailsDto,
  WorkoutDto,
  WorkoutGroups,
} from '../../../common/workout';
import { AuthService } from '../../../services/auth-service';
import { WorkoutService } from '../../../services/workout-service';

@Component({
  selector: 'app-active-workout',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './active-workout.html',
})
export class ActiveWorkout implements OnInit {
  // Data
  workoutGroups: WorkoutGroups = {};
  workoutNames: string[] = [];
  filteredWorkoutNames: string[] = [];
  workoutDetails: WorkoutDetailsDto[] = [];
  sets: SetDto[] = [];
  exercises: ExerciseDto[] = [];
  isFinishingWorkout = false;
  finishWorkoutError: string | null = null;

  // Pagination
  currentPage = 0;
  pageSize = 5;
  searchQuery = '';

  // State
  selectedWorkout: WorkoutDto | null = null;
  selectedDetail: WorkoutDetailsDto | null = null;
  userUUID = '';
  newWorkoutName = '';

  // Modal
  showAddExerciseModal = false;
  selectedExerciseId: number | null = null;

  // Active set controls
  currentKg = 0;
  currentReps = 0;

  constructor(
    private workoutService: WorkoutService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private destroyRef: DestroyRef,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const workoutIdFromUrl = this.route.snapshot.queryParamMap.get('workoutId');

    this.authService
      .getMe()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((user) => {
        this.userUUID = user.id;
        this.syncView();

        this.workoutService
          .getUserWorkouts(this.userUUID)
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((groups) => {
            this.workoutGroups = groups;
            this.workoutNames = Object.keys(groups);
            this.filteredWorkoutNames = this.workoutNames;

            if (workoutIdFromUrl) {
              this.openWorkoutById(workoutIdFromUrl);
              return;
            }

            this.syncView();
          });

        this.workoutService
          .getExercises()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((exercises) => {
            this.exercises = exercises;
            this.syncView();
          });
      });
  }

  get isWorkoutFinished(): boolean {
    return String(this.selectedWorkout?.status ?? '').toUpperCase() === 'FINISHED';
  }

  get canEditWorkout(): boolean {
    return !!this.selectedWorkout && !this.isWorkoutFinished;
  }

  private openWorkoutById(workoutId: string): void {
    const workout = this.findWorkoutById(workoutId);

    if (workout) {
      this.selectWorkout(workout);
      return;
    }

    console.warn('Workout from URL was not found in workout groups:', workoutId);

    this.selectedWorkout = {
      id: workoutId,
      workoutName: 'Active workout',
    } as WorkoutDto;

    this.selectedDetail = null;
    this.sets = [];
    this.syncView();

    this.workoutService
      .getWorkoutDetails(workoutId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((details) => {
        this.workoutDetails = details;
        this.syncView();
      });
  }

  private findWorkoutById(workoutId: string): WorkoutDto | null {
    const allWorkouts = Object.values(this.workoutGroups).reduce(
      (result, group) => [...result, ...group],
      [] as WorkoutDto[]
    );

    return allWorkouts.find((workout) => workout.id === workoutId) ?? null;
  }

  // Step 1: Workout

  get suggestedWorkoutNames(): string[] {
    if (!this.newWorkoutName.trim()) return [];

    return this.workoutNames.filter((name) =>
      name.toLowerCase().includes(this.newWorkoutName.toLowerCase())
    );
  }

  createNewWorkout(): void {
    if (!this.newWorkoutName.trim()) return;

    this.workoutService
      .createWorkout(this.userUUID, this.newWorkoutName)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (workout) => {
          const name = workout.workoutName.trim();

          if (!this.workoutGroups[name]) {
            this.workoutGroups[name] = [];
            this.workoutNames = Object.keys(this.workoutGroups);
            this.filteredWorkoutNames = this.workoutNames;
          }

          this.workoutGroups[name].push(workout);
          this.selectWorkout(workout);
          this.syncView();
        },
        error: (error) => console.error(error),
      });
  }

  selectWorkout(workout: WorkoutDto): void {
    this.selectedWorkout = workout;
    this.selectedDetail = null;
    this.sets = [];
    this.finishWorkoutError = null;
    this.closeAddExercise();
    this.syncView();

    this.workoutService
      .getWorkoutDetails(workout.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((details) => {
        this.workoutDetails = details;
        this.syncView();
      });
  }

  // Step 2: Exercises

  get filteredExercises(): ExerciseDto[] {
    if (!this.searchQuery.trim()) return this.exercises;

    return this.exercises.filter(
      (exercise) =>
        exercise.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        exercise.muscleGroup.toLowerCase().includes(this.searchQuery.toLowerCase())
    );
  }

  get pagedExercises(): ExerciseDto[] {
    const start = this.currentPage * this.pageSize;
    return this.filteredExercises.slice(start, start + this.pageSize);
  }

  get totalPages(): number {
    return Math.ceil(this.filteredExercises.length / this.pageSize);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }

  onSearch(): void {
    this.currentPage = 0;
  }

  selectDetail(detail: WorkoutDetailsDto): void {
    this.selectedDetail = detail;
    this.currentKg = 0;
    this.currentReps = 0;
    this.syncView();

    this.workoutService
      .getSets(detail.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((sets) => {
        this.sets = sets;
        this.syncView();
      });
  }

  addSelectedExercise(): void {
    if (!this.canEditWorkout || !this.selectedWorkout || !this.selectedExerciseId) {
      return;
    }

    this.workoutService
      .addWorkoutDetail(this.selectedWorkout.id, this.selectedExerciseId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (detail) => {
          this.workoutDetails = [...this.workoutDetails, detail];
          this.selectDetail(detail);
          this.closeAddExercise();
          this.syncView();
        },
        error: (error) => console.error(error),
      });
  }

  openAddExercise(): void {
    if (!this.canEditWorkout) {
      return;
    }

    this.showAddExerciseModal = true;
    this.currentPage = 0;
    this.searchQuery = '';
    this.selectedExerciseId = null;
  }

  closeAddExercise(): void {
    this.showAddExerciseModal = false;
    this.selectedExerciseId = null;
    this.searchQuery = '';
    this.currentPage = 0;
  }

  // Step 3: Sets

  increaseKg(): void {
    if (!this.canEditWorkout) return;

    this.currentKg = Math.round((this.currentKg + 2.5) * 10) / 10;
  }

  decreaseKg(): void {
    if (!this.canEditWorkout) return;

    this.currentKg = Math.max(0, Math.round((this.currentKg - 2.5) * 10) / 10);
  }

  increaseReps(): void {
    if (!this.canEditWorkout) return;

    this.currentReps++;
  }

  decreaseReps(): void {
    if (!this.canEditWorkout) return;

    this.currentReps = Math.max(0, this.currentReps - 1);
  }

  logSet(): void {
    if (!this.canEditWorkout || !this.selectedDetail || this.currentKg <= 0 || this.currentReps <= 0) {
      return;
    }

    const set: SetDto = {
      id: 0,
      reps: this.currentReps,
      kg: this.currentKg,
      setNumber: this.sets.length + 1,
      workoutDetailsId: this.selectedDetail.id,
    };

    this.workoutService
      .saveSet(set)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({          
          next: (saved) => {
            this.sets = [...this.sets, saved];
            this.syncView();
          },
          error: (error) => console.error(error),
        });
  }

  finishWorkout(): void {
    if (!this.selectedWorkout || this.isWorkoutFinished) {
      return;
    }

    if (this.workoutDetails.length === 0) {
      this.finishWorkoutError = 'Не можеш да приключиш тренировка без упражнения.';
      this.syncView();
      return;
    }

    this.isFinishingWorkout = true;
    this.finishWorkoutError = null;
    this.syncView();

    this.workoutService
      .finishWorkout(this.selectedWorkout.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (finishedWorkout) => {
          this.selectedWorkout = {
            ...this.selectedWorkout!,
            status: finishedWorkout.status,
          };

          this.isFinishingWorkout = false;

          this.selectedWorkout = null;
          this.selectedDetail = null;
          this.workoutDetails = [];
          this.sets = [];

          this.reloadWorkouts();
          this.syncView();
        },
        error: (error) => {
          console.error('Could not finish workout:', error);

          if (typeof error.error === 'string') {
            this.finishWorkoutError = error.error;
          } else {
            this.finishWorkoutError =
              error?.error?.error ||
              error?.error?.message ||
              error?.message ||
              'Не успяхме да приключим тренировката.';
          }

          this.isFinishingWorkout = false;
          this.syncView();
        },
      });
  }

  private reloadWorkouts(): void {
    if (!this.userUUID) {
      return;
    }

    this.workoutService
      .getUserWorkouts(this.userUUID)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((groups) => {
        this.workoutGroups = groups;
        this.workoutNames = Object.keys(groups);
        this.filteredWorkoutNames = this.workoutNames;
        this.syncView();
      });
  }

  private syncView(): void {
    this.cdr.markForCheck();
  }
}