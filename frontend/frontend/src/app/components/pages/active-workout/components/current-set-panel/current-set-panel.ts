import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { SetDto, WorkoutDetailsDto } from '../../../../../common/workout';

@Component({
  selector: 'app-current-set-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './current-set-panel.html',
})
export class CurrentSetPanel {
  @Input() selectedDetail: WorkoutDetailsDto | null = null;
  @Input() sets: SetDto[] = [];
  @Input() canEditWorkout = false;
  @Input() isWorkoutFinished = false;
  @Input() setMutationError: string | null = null;

  @Input() currentKg = 0;
  @Input() currentReps = 0;

  @Input() deletingSetId: number | null = null;
  @Input() savingEditedSetId: number | null = null;

  @Output() currentKgChange = new EventEmitter<number>();
  @Output() currentRepsChange = new EventEmitter<number>();

  @Output() logSet = new EventEmitter<void>();
  @Output() updateSet = new EventEmitter<SetDto>();
  @Output() deleteSet = new EventEmitter<SetDto>();

  editingSetId: number | null = null;
  editKg = 0;
  editReps = 0;

  increaseKg(): void {
    if (!this.canEditWorkout) return;

    this.currentKgChange.emit(Math.round((this.currentKg + 1.25) * 100) / 100);
  }

  decreaseKg(): void {
    if (!this.canEditWorkout) return;

    this.currentKgChange.emit(Math.max(0, Math.round((this.currentKg - 1.25) * 100) / 100));
  }

  increaseReps(): void {
    if (!this.canEditWorkout) return;

    this.currentRepsChange.emit(this.currentReps + 1);
  }

  decreaseReps(): void {
    if (!this.canEditWorkout) return;

    this.currentRepsChange.emit(Math.max(0, this.currentReps - 1));
  }

  startEditSet(set: SetDto): void {
    if (!this.canEditWorkout) return;

    this.editingSetId = set.id;
    this.editKg = set.kg;
    this.editReps = set.reps;
  }

  cancelEditSet(): void {
    this.editingSetId = null;
    this.editKg = 0;
    this.editReps = 0;
  }

  saveEditedSet(set: SetDto): void {
    if (!this.canEditWorkout || this.editKg <= 0 || this.editReps <= 0) {
      return;
    }

    this.updateSet.emit({
      ...set,
      kg: Math.round(Number(this.editKg) * 10) / 10,
      reps: Math.floor(Number(this.editReps)),
    });

    this.cancelEditSet();
  }

  setKgFromInput(value: number | string | null): void {
  if (!this.canEditWorkout) {
    return;
  }

  const kg = Number(value);

  if (!Number.isFinite(kg) || kg < 0) {
    this.currentKgChange.emit(0);
    return;
  }

  this.currentKgChange.emit(Math.round(kg * 10) / 10);
}

setRepsFromInput(value: number | string | null): void {
  if (!this.canEditWorkout) {
    return;
  }

  const reps = Number(value);

  if (!Number.isFinite(reps) || reps < 0) {
    this.currentRepsChange.emit(0);
    return;
  }

  this.currentRepsChange.emit(Math.floor(reps));
}
}