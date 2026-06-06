import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

type WorkoutSet = {
  setNumber: number;
  previous: string;
  kg: number | null;
  reps: number | null;
  done: boolean;
  active?: boolean;
};

type Exercise = {
  name: string;
  type: string;
  completed: boolean;
  active: boolean;
  sets: WorkoutSet[];
};

type ExerciseTemplate = {
  name: string;
  type: string;
  muscle: string;
  last: string;
};

@Component({
  selector: 'app-active-workout',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './active-workout.html',
  styleUrl: './active-workout.scss',
})
export class ActiveWorkout {
  workoutTitle = 'Monday Push A';
  duration = '57:21';
  showAddExerciseModal = false;
  selectedExerciseName = 'Incline Dumbbell Press';

  exerciseLibrary: ExerciseTemplate[] = [
    {
      name: 'Incline Dumbbell Press',
      type: 'Dumbbell',
      muscle: 'Chest',
      last: '30kg x 10',
    },
    {
      name: 'Cable Fly',
      type: 'Cable',
      muscle: 'Chest',
      last: '32.5kg x 14',
    },
    {
      name: 'Lateral Raise',
      type: 'Dumbbell',
      muscle: 'Shoulders',
      last: '12.5kg x 15',
    },
    {
      name: 'Triceps Pushdown',
      type: 'Cable',
      muscle: 'Triceps',
      last: '45kg x 12',
    },
  ];

  exercises: Exercise[] = [
    {
      name: 'Bench Press',
      type: 'Barbell',
      completed: true,
      active: false,
      sets: [
        {
          setNumber: 1,
          previous: '80kg x 10',
          kg: 85,
          reps: 10,
          done: true,
        },
        {
          setNumber: 2,
          previous: '90kg x 8',
          kg: 90,
          reps: 8,
          done: true,
        },
      ],
    },
    {
      name: 'Overhead Press',
      type: 'Dumbbell',
      completed: false,
      active: true,
      sets: [
        {
          setNumber: 1,
          previous: '20kg x 12',
          kg: 22.5,
          reps: 12,
          done: true,
        },
        {
          setNumber: 2,
          previous: '25kg x 10',
          kg: 25,
          reps: 10,
          done: false,
          active: true,
        },
        {
          setNumber: 3,
          previous: '25kg x 8',
          kg: null,
          reps: null,
          done: false,
        },
      ],
    },
  ];

  get completedExercises(): number {
    return this.exercises.filter((exercise) => exercise.completed).length;
  }

  get totalExercises(): number {
    return this.exercises.length;
  }

  get progressPercentage(): number {
    return this.totalExercises ? (this.completedExercises / this.totalExercises) * 100 : 0;
  }

  get completedSets(): number {
    return this.exercises.flatMap((exercise) => exercise.sets).filter((set) => set.done).length;
  }

  get totalSets(): number {
    return this.exercises.flatMap((exercise) => exercise.sets).length;
  }

  get setProgressPercentage(): number {
    return this.totalSets ? (this.completedSets / this.totalSets) * 100 : 0;
  }

  get activeExercise(): Exercise | undefined {
    return this.exercises.find((exercise) => exercise.active);
  }

  get activeSet(): WorkoutSet | undefined {
    return this.activeExercise?.sets.find((set) => set.active);
  }

  get activeExerciseCompletedSets(): number {
    return this.activeExercise?.sets.filter((set) => set.done).length ?? 0;
  }

  get activeExerciseTotalSets(): number {
    return this.activeExercise?.sets.length ?? 0;
  }

  get activeExerciseProgressPercentage(): number {
    return this.activeExerciseTotalSets
      ? (this.activeExerciseCompletedSets / this.activeExerciseTotalSets) * 100
      : 0;
  }

  exerciseCompletedSets(exercise: Exercise): number {
    return exercise.sets.filter((set) => set.done).length;
  }

  exerciseProgressPercentage(exercise: Exercise): number {
    return exercise.sets.length ? (this.exerciseCompletedSets(exercise) / exercise.sets.length) * 100 : 0;
  }

  increaseKg(set: WorkoutSet): void {
    set.kg = (set.kg ?? 0) + 2.5;
  }

  decreaseKg(set: WorkoutSet): void {
    const current = set.kg ?? 0;
    set.kg = Math.max(0, current - 2.5);
  }

  increaseReps(set: WorkoutSet): void {
    set.reps = (set.reps ?? 0) + 1;
  }

  decreaseReps(set: WorkoutSet): void {
    const current = set.reps ?? 0;
    set.reps = Math.max(0, current - 1);
  }

  repeatLastSet(exercise: Exercise, set: WorkoutSet): void {
    const lastDoneSet = [...exercise.sets].reverse().find((item) => item.done);

    if (!lastDoneSet) {
      return;
    }

    set.kg = lastDoneSet.kg;
    set.reps = lastDoneSet.reps;
    set.previous = `${lastDoneSet.kg ?? '-'}kg x ${lastDoneSet.reps ?? '-'}`;
  }

  logSet(exercise: Exercise, set: WorkoutSet): void {
    set.done = true;
    set.active = false;

    const nextSet = exercise.sets.find((item) => !item.done);

    if (nextSet) {
      nextSet.active = true;

      if (nextSet.kg === null) {
        nextSet.kg = set.kg;
      }

      if (nextSet.reps === null) {
        nextSet.reps = set.reps;
      }

      return;
    }

    exercise.completed = true;
    exercise.active = false;

    const nextExercise = this.exercises.find((item) => !item.completed);

    if (nextExercise) {
      nextExercise.active = true;

      const nextExerciseSet = nextExercise.sets.find((item) => !item.done);

      if (nextExerciseSet) {
        nextExerciseSet.active = true;
      }
    }
  }

  addSet(exercise: Exercise): void {
    const lastSet = exercise.sets[exercise.sets.length - 1];

    exercise.sets.push({
      setNumber: exercise.sets.length + 1,
      previous: lastSet ? `${lastSet.kg ?? '-'}kg x ${lastSet.reps ?? '-'}` : '-',
      kg: lastSet?.kg ?? null,
      reps: lastSet?.reps ?? null,
      done: false,
      active: !exercise.sets.some((set) => set.active),
    });
  }

  openAddExercise(): void {
    this.showAddExerciseModal = true;
  }

  closeAddExercise(): void {
    this.showAddExerciseModal = false;
  }

  get selectedExercise(): ExerciseTemplate {
    return (
      this.exerciseLibrary.find((exercise) => exercise.name === this.selectedExerciseName) ??
      this.exerciseLibrary[0]
    );
  }

  addSelectedExercise(): void {
    const template = this.selectedExercise;
    const hasActiveExercise = this.exercises.some((exercise) => exercise.active && !exercise.completed);

    this.exercises.push({
      name: template.name,
      type: template.type,
      completed: false,
      active: !hasActiveExercise,
      sets: [
        {
          setNumber: 1,
          previous: template.last,
          kg: null,
          reps: null,
          done: false,
          active: !hasActiveExercise,
        },
        {
          setNumber: 2,
          previous: template.last,
          kg: null,
          reps: null,
          done: false,
        },
        {
          setNumber: 3,
          previous: template.last,
          kg: null,
          reps: null,
          done: false,
        },
      ],
    });

    this.closeAddExercise();
  }
}
