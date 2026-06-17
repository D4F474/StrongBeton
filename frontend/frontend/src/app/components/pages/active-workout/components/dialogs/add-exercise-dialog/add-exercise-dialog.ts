import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ExerciseDto } from '../../../../../../common/workout';

@Component({
  selector: 'app-add-exercise-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-exercise-dialog.html',
})
export class AddExerciseDialog {
  @Input() exercises: ExerciseDto[] = [];
  @Input() canEditWorkout = false;

  @Output() close = new EventEmitter<void>();
  @Output() addExercise = new EventEmitter<number>();

  selectedExerciseId: number | null = null;
  searchQuery = '';
  currentPage = 0;
  pageSize = 5;

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
    return Math.max(1, Math.ceil(this.filteredExercises.length / this.pageSize));
  }

  onSearch(): void {
    this.currentPage = 0;
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

  submit(): void {
    if (!this.selectedExerciseId || !this.canEditWorkout) {
      return;
    }

    this.addExercise.emit(this.selectedExerciseId);
  }
}
