import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

import { WorkoutDetailsDto, WorkoutDto } from '../../../../../common/workout';

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
}