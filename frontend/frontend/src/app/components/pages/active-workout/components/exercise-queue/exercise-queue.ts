import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

import { WorkoutDetailsDto } from '../../../../../common/workout';

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

  onDelete(detail: WorkoutDetailsDto, event: MouseEvent): void {
    event.stopPropagation();
    this.deleteDetail.emit(detail);
  }
}