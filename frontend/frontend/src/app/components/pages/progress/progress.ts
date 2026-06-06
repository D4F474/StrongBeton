import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-progress',
  imports: [CommonModule],
  templateUrl: './progress.html',
  styleUrl: './progress.scss',
})
export class Progress {
  metrics = [
    { label: 'Strength Score', value: '766', sublabel: '+24 after last workout', tone: 'green' },
    { label: 'Weekly Volume', value: '42,800 kg', sublabel: '85% of target', tone: 'gold' },
    { label: 'Training Streak', value: '6 days', sublabel: 'Best streak: 18', tone: 'gold' },
    { label: 'Personal Records', value: '14', sublabel: '3 this month', tone: 'green' },
  ];

  volumeBars = [
    { label: 'Mon', value: 82 },
    { label: 'Tue', value: 36 },
    { label: 'Wed', value: 68 },
    { label: 'Thu', value: 44 },
    { label: 'Fri', value: 96 },
    { label: 'Sat', value: 52 },
    { label: 'Sun', value: 20 },
  ];

  lifts = [
    { name: 'Bench Press', current: '120kg x 5', previous: '115kg x 5', progress: 86 },
    { name: 'Squat', current: '155kg x 3', previous: '150kg x 3', progress: 78 },
    { name: 'Deadlift', current: '190kg x 2', previous: '185kg x 2', progress: 92 },
    { name: 'Overhead Press', current: '72.5kg x 4', previous: '70kg x 4', progress: 64 },
  ];

  records = [
    { lift: 'Incline Dumbbell Press', result: '32.5kg x 9', date: 'Today' },
    { lift: 'Bench Press', result: '120kg x 5', date: 'May 29' },
    { lift: 'Triceps Pushdown', result: '55kg x 11', date: 'May 26' },
  ];
}
