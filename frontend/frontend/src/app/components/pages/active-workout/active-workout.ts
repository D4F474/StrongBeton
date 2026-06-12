import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
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
import { AddExerciseDialog } from './components/dialogs/add-exercise-dialog/add-exercise-dialog';
import { CurrentSetPanel } from './components/current-set-panel/current-set-panel';
import { ExerciseQueue } from './components/exercise-queue/exercise-queue';
import { WorkoutPicker } from './components/workout-picker/workout-picker';
import { WorkoutTopbar } from './components/workout-topbar/workout-topbar';

@Component({
  selector: 'app-active-workout',
  standalone: true,
  imports: [
    CommonModule,
    AddExerciseDialog,
    CurrentSetPanel,
    ExerciseQueue,
    WorkoutPicker,
    WorkoutTopbar,
  ],
  templateUrl: './active-workout.html',
})
export class ActiveWorkout implements OnInit {
  workoutGroups: WorkoutGroups = {};
  workoutNames: string[] = [];
  workoutDetails: WorkoutDetailsDto[] = [];
  sets: SetDto[] = [];
  exercises: ExerciseDto[] = [];

  selectedWorkout: WorkoutDto | null = null;
  selectedDetail: WorkoutDetailsDto | null = null;
  userUUID = '';

  currentKg = 0;
  currentReps = 0;

  showAddExerciseModal = false;
  isCreatingWorkout = false;
  isFinishingWorkout = false;
  deletingWorkoutDetailId: number | null = null;
  deletingSetId: number | null = null;
  savingEditedSetId: number | null = null;

  createWorkoutError: string | null = null;
  exerciseMutationError: string | null = null;
  setMutationError: string | null = null;
  finishWorkoutError: string | null = null;

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
      .subscribe({
        next: (user) => {
          this.userUUID = user.id;
          this.loadWorkouts(workoutIdFromUrl);
          this.loadExercises();
        },
        error: (error) => {
          this.createWorkoutError = this.getApiErrorMessage(error, 'Could not load your profile.');
          this.syncView();
        },
      });
  }

  get isWorkoutFinished(): boolean {
    return String(this.selectedWorkout?.status ?? '').toUpperCase() === 'FINISHED';
  }

  get canEditWorkout(): boolean {
    return !!this.selectedWorkout && !this.isWorkoutFinished;
  }

  createNewWorkout(workoutName: string): void {
    if (!workoutName.trim() || !this.userUUID || this.isCreatingWorkout) {
      return;
    }

    this.isCreatingWorkout = true;
    this.createWorkoutError = null;
    this.syncView();

    this.workoutService
      .createWorkout(this.userUUID, workoutName.trim())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (workout) => {
          this.addWorkoutToGroups(workout);
          this.selectWorkout(workout);
          this.isCreatingWorkout = false;
          this.syncView();
        },
        error: (error) => {
          this.createWorkoutError = this.getApiErrorMessage(
            error,
            'Could not create workout.'
          );
          this.isCreatingWorkout = false;
          this.syncView();
        },
      });
  }

  selectWorkout(workout: WorkoutDto): void {
    this.selectedWorkout = workout;
    this.selectedDetail = null;
    this.workoutDetails = [];
    this.sets = [];
    this.finishWorkoutError = null;
    this.exerciseMutationError = null;
    this.setMutationError = null;
    this.closeAddExercise();
    this.syncView();

    this.loadWorkoutDetails(workout.id);
  }

  selectDetail(detail: WorkoutDetailsDto): void {
    this.selectedDetail = detail;
    this.currentKg = 0;
    this.currentReps = 0;
    this.sets = [];
    this.setMutationError = null;
    this.syncView();

    this.workoutService
      .getSets(detail.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (sets) => {
          this.sets = sets;
          this.syncView();
        },
        error: (error) => {
          this.setMutationError = this.getApiErrorMessage(error, 'Could not load sets.');
          this.syncView();
        },
      });
  }

  goBack(): void {
    if (this.selectedDetail) {
      this.selectedDetail = null;
      this.sets = [];
      this.setMutationError = null;
      this.syncView();
      return;
    }

    this.selectedWorkout = null;
    this.workoutDetails = [];
    this.finishWorkoutError = null;
    this.exerciseMutationError = null;
    this.closeAddExercise();
    this.syncView();
  }

  openAddExercise(): void {
    if (!this.canEditWorkout) {
      return;
    }

    this.showAddExerciseModal = true;
    this.exerciseMutationError = null;
    this.syncView();
  }

  closeAddExercise(): void {
    this.showAddExerciseModal = false;
  }

  addSelectedExercise(exerciseId: number): void {
    if (!this.canEditWorkout || !this.selectedWorkout) {
      return;
    }

    this.exerciseMutationError = null;
    this.syncView();

    this.workoutService
      .addWorkoutDetail(this.selectedWorkout.id, exerciseId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (detail) => {
          this.upsertWorkoutDetail(detail);
          this.selectDetail(detail);
          this.closeAddExercise();
          this.syncView();
        },
        error: (error) => {
          this.exerciseMutationError = this.getApiErrorMessage(
            error,
            'Could not add exercise.'
          );
          this.syncView();
        },
      });
  }

  deleteWorkoutDetail(detail: WorkoutDetailsDto): void {
    if (!this.canEditWorkout || this.deletingWorkoutDetailId !== null) {
      return;
    }

    this.deletingWorkoutDetailId = detail.id;
    this.exerciseMutationError = null;
    this.syncView();

    this.workoutService
      .deleteWorkoutDetail(detail.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.workoutDetails = this.workoutDetails.filter((item) => item.id !== detail.id);

          if (this.selectedDetail?.id === detail.id) {
            this.selectedDetail = null;
            this.sets = [];
          }

          this.deletingWorkoutDetailId = null;
          this.syncView();
        },
        error: (error) => {
          this.exerciseMutationError = this.getApiErrorMessage(
            error,
            'Could not delete exercise.'
          );
          this.deletingWorkoutDetailId = null;
          this.syncView();
        },
      });
  }

  logSet(): void {
    if (!this.canEditWorkout || !this.selectedDetail || this.currentKg <= 0 || this.currentReps <= 0) {
      return;
    }

    const set: SetDto = {
      id: 0,
      reps: Math.floor(Number(this.currentReps)),
      kg: Math.round(Number(this.currentKg) * 10) / 10,
      setNumber: this.sets.length + 1,
      workoutDetailsId: this.selectedDetail.id,
    };

    this.setMutationError = null;
    this.syncView();

    this.workoutService
      .saveSet(set)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (saved) => {
          this.sets = [...this.sets, saved];
          this.currentKg = 0;
          this.currentReps = 0;
          this.reloadSelectedDetail();
          this.syncView();
        },
        error: (error) => {
          this.setMutationError = this.getApiErrorMessage(error, 'Could not save set.');
          this.syncView();
        },
      });
  }

  updateSet(set: SetDto): void {
    if (!this.canEditWorkout || this.savingEditedSetId !== null) {
      return;
    }

    this.savingEditedSetId = set.id;
    this.setMutationError = null;
    this.syncView();

    this.workoutService
      .updateSet(set)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (saved) => {
          this.sets = this.sets.map((item) => (item.id === saved.id ? saved : item));
          this.savingEditedSetId = null;
          this.reloadSelectedDetail();
          this.syncView();
        },
        error: (error) => {
          this.setMutationError = this.getApiErrorMessage(error, 'Could not update set.');
          this.savingEditedSetId = null;
          this.syncView();
        },
      });
  }

  deleteSet(set: SetDto): void {
    if (!this.canEditWorkout || this.deletingSetId !== null) {
      return;
    }

    this.deletingSetId = set.id;
    this.setMutationError = null;
    this.syncView();

    this.workoutService
      .deleteSet(set.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.sets = this.reorderSets(
            this.sets.filter((item) => item.id !== set.id)
          );
          this.deletingSetId = null;
          this.reloadSelectedDetail();
          this.syncView();
        },
        error: (error) => {
          this.setMutationError = this.getApiErrorMessage(error, 'Could not delete set.');
          this.deletingSetId = null;
          this.syncView();
        },
      });
  }

  finishWorkout(): void {
    if (!this.selectedWorkout || this.isWorkoutFinished) {
      return;
    }

    if (this.workoutDetails.length === 0) {
      this.finishWorkoutError = 'Add at least one exercise before finishing the workout.';
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
        next: () => {
          this.isFinishingWorkout = false;
          this.selectedWorkout = null;
          this.selectedDetail = null;
          this.workoutDetails = [];
          this.sets = [];
          this.loadWorkouts();
          this.syncView();
        },
        error: (error) => {
          this.finishWorkoutError = this.getApiErrorMessage(
            error,
            'Could not finish workout.'
          );
          this.isFinishingWorkout = false;
          this.syncView();
        },
      });
  }

  private loadWorkouts(workoutIdToOpen?: string | null): void {
    if (!this.userUUID) {
      return;
    }

    this.workoutService
      .getUserWorkouts(this.userUUID)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (groups) => {
          this.workoutGroups = groups;
          this.workoutNames = Object.keys(groups);

          if (workoutIdToOpen) {
            this.openWorkoutById(workoutIdToOpen);
          }

          this.syncView();
        },
        error: (error) => {
          this.createWorkoutError = this.getApiErrorMessage(error, 'Could not load workouts.');
          this.syncView();
        },
      });
  }

  private loadExercises(): void {
    this.workoutService
      .getExercises()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (exercises) => {
          this.exercises = exercises;
          this.syncView();
        },
        error: (error) => {
          this.exerciseMutationError = this.getApiErrorMessage(
            error,
            'Could not load exercises.'
          );
          this.syncView();
        },
      });
  }

  private loadWorkoutDetails(workoutId: string): void {
    this.workoutService
      .getWorkoutDetails(workoutId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (details) => {
          this.workoutDetails = details;
          this.syncView();
        },
        error: (error) => {
          this.exerciseMutationError = this.getApiErrorMessage(
            error,
            'Could not load workout exercises.'
          );
          this.syncView();
        },
      });
  }

  private openWorkoutById(workoutId: string): void {
    const workout = this.findWorkoutById(workoutId);

    if (workout) {
      this.selectWorkout(workout);
      return;
    }

    this.selectedWorkout = {
      id: workoutId,
      workoutName: 'Active workout',
      date: '',
      status: 'DRAFT',
    };
    this.selectedDetail = null;
    this.sets = [];
    this.loadWorkoutDetails(workoutId);
    this.syncView();
  }

  private findWorkoutById(workoutId: string): WorkoutDto | null {
    const allWorkouts = Object.values(this.workoutGroups).flat();

    return allWorkouts.find((workout) => workout.id === workoutId) ?? null;
  }

  private addWorkoutToGroups(workout: WorkoutDto): void {
    const workoutName = workout.workoutName.trim();
    const currentGroup = this.workoutGroups[workoutName] ?? [];

    this.workoutGroups = {
      ...this.workoutGroups,
      [workoutName]: [...currentGroup, workout],
    };
    this.workoutNames = Object.keys(this.workoutGroups);
  }

  private upsertWorkoutDetail(detail: WorkoutDetailsDto): void {
    const exists = this.workoutDetails.some((item) => item.id === detail.id);

    this.workoutDetails = exists
      ? this.workoutDetails.map((item) => (item.id === detail.id ? detail : item))
      : [...this.workoutDetails, detail];
  }

  private reloadSelectedDetail(): void {
    if (!this.selectedWorkout || !this.selectedDetail) {
      return;
    }

    this.workoutService
      .getWorkoutDetails(this.selectedWorkout.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (details) => {
          this.workoutDetails = details;
          this.selectedDetail =
            details.find((detail) => detail.id === this.selectedDetail?.id) ?? this.selectedDetail;
          this.syncView();
        },
      });
  }

  private reorderSets(sets: SetDto[]): SetDto[] {
    return sets.map((set, index) => ({
      ...set,
      setNumber: index + 1,
    }));
  }

  private getApiErrorMessage(error: unknown, fallback: string): string {
    if (typeof error !== 'object' || error === null) {
      return fallback;
    }

    const httpError = error as {
      error?: unknown;
      message?: string;
      status?: number;
    };

    if (typeof httpError.error === 'string') {
      return httpError.error;
    }

    if (typeof httpError.error === 'object' && httpError.error !== null) {
      const problem = httpError.error as {
        detail?: string;
        description?: string;
        message?: string;
        error?: string;
        title?: string;
      };

      return (
        problem.detail ||
        problem.description ||
        problem.message ||
        problem.error ||
        problem.title ||
        fallback
      );
    }

    if (httpError.status === 0) {
      return 'Cannot connect to the server.';
    }

    return httpError.message || fallback;
  }

  private syncView(): void {
    this.cdr.markForCheck();
  }
}
