import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

import { ExerciseDto, WorkoutDetailsDto, WorkoutDto } from '../../../../../common/workout';

@Component({
  selector: 'app-workout-topbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './workout-topbar.html',
})
export class WorkoutTopbar {
  @Input() selectedWorkout: WorkoutDto | null = null;
  @Input() selectedDetail: WorkoutDetailsDto | null = null;
  @Input() canEditWorkout = false;
  @Input() isWorkoutFinished = false;
  @Input() isFinishingWorkout = false;
  @Input() workoutDetailsCount = 0;

  @Output() back = new EventEmitter<void>();
  @Output() addExercise = new EventEmitter<void>();
  @Output() finishWorkout = new EventEmitter<void>();

  hideBrokenImage(event: Event): void {
    const image = event.target as HTMLImageElement;
    image.hidden = true;
  }

  getExerciseImageUrl(exercise: ExerciseDto): string {
    if (exercise.imageUrl) {
      return exercise.imageUrl;
    }

    return `assets/exercises/${this.slugifyExerciseName(exercise.name)}.png`;
  }

  private slugifyExerciseName(name: string): string {
    return name
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-+|-+$/g, '');
  }
}
