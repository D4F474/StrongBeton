import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

import { ExerciseDto, WorkoutDetailsDto } from '../../../../../common/workout';

@Component({
  selector: 'app-exercise-queue',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exercise-queue.html',
})
export class ExerciseQueue {
  @Input() workoutDetails: WorkoutDetailsDto[] = [];
  @Input() canEditWorkout = false;
  @Input() isWorkoutFinished = false;
  @Input() deletingWorkoutDetailId: number | null = null;
  @Input() exerciseMutationError: string | null = null;

  @Output() selectDetail = new EventEmitter<WorkoutDetailsDto>();
  @Output() deleteDetail = new EventEmitter<WorkoutDetailsDto>();

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

  onDelete(detail: WorkoutDetailsDto, event: MouseEvent): void {
    event.stopPropagation();
    this.deleteDetail.emit(detail);
  }
}
