import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { WorkoutDto, WorkoutGroups } from '../../../../../common/workout';

@Component({
  selector: 'app-workout-picker',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './workout-picker.html',
})
export class WorkoutPicker {
  @Input() workoutGroups: WorkoutGroups = {};
  @Input() workoutNames: string[] = [];
  @Input() isCreatingWorkout = false;
  @Input() createWorkoutError: string | null = null;

  @Output() createWorkout = new EventEmitter<string>();
  @Output() selectWorkout = new EventEmitter<WorkoutDto>();

  newWorkoutName = '';

  get suggestedWorkoutNames(): string[] {
    const query = this.newWorkoutName.trim().toLowerCase();

    if (!query) {
      return [];
    }

    return this.workoutNames.filter((name) => name.toLowerCase().includes(query));
  }

  submitWorkout(): void {
    const workoutName = this.newWorkoutName.trim();

    if (!workoutName || this.isCreatingWorkout) {
      return;
    }

    this.createWorkout.emit(workoutName);
    this.newWorkoutName = '';
  }
}
