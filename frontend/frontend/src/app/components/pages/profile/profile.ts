import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, RouterLink],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile {
  stats = [
    { label: 'Strength Score', value: '766' },
    { label: 'Workouts', value: '184' },
    { label: 'Best Streak', value: '18' },
    { label: 'Records', value: '42' },
  ];

  badges = ['PR Hunter', 'Iron Legion Captain', 'Volume Builder', '6 Day Streak', 'Old School'];

  history = [
    { title: 'Monday Push A', date: 'Today', volume: '18,450 kg', score: '+24' },
    { title: 'Lower Strength', date: 'May 31', volume: '24,200 kg', score: '+18' },
    { title: 'Pull Density', date: 'May 29', volume: '19,900 kg', score: '+12' },
  ];
}
