import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-workout-summary',
  imports: [CommonModule, RouterLink],
  templateUrl: './workout-summary.html',
  styleUrl: './workout-summary.scss',
})
export class WorkoutSummary {
  highlights = [
    { label: 'Total Volume', value: '18,450 kg', change: '+1,280 vs last push' },
    { label: 'Working Sets', value: '22', change: '19 logged clean' },
    { label: 'Duration', value: '58:14', change: '7 min faster' },
    { label: 'Score Gain', value: '+24', change: 'Strength score 766' },
  ];

  exercises = [
    { name: 'Bench Press', topSet: '95kg x 6', sets: '4 sets', volume: '4,120 kg', pr: true },
    { name: 'Overhead Press', topSet: '27.5kg x 10', sets: '4 sets', volume: '2,080 kg', pr: false },
    { name: 'Incline Dumbbell Press', topSet: '32.5kg x 9', sets: '3 sets', volume: '1,755 kg', pr: true },
    { name: 'Cable Fly', topSet: '35kg x 14', sets: '3 sets', volume: '1,360 kg', pr: false },
    { name: 'Triceps Pushdown', topSet: '50kg x 12', sets: '4 sets', volume: '2,140 kg', pr: false },
  ];

  badges = ['Push Day Cleared', 'Bench PR', 'Volume Builder'];
}
