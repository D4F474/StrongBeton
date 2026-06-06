import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clans',
  imports: [CommonModule],
  templateUrl: './clans.html',
  styleUrl: './clans.scss',
})
export class Clans {
  clans = [
    { rank: 1, name: 'Iron Legion', members: 18, score: 12840, volume: '328,900 kg', trend: '+12%' },
    { rank: 2, name: 'Concrete Crew', members: 16, score: 11920, volume: '301,200 kg', trend: '+7%' },
    { rank: 3, name: 'Barbell Union', members: 20, score: 10870, volume: '287,500 kg', trend: '+4%' },
    { rank: 4, name: 'Rep Foundry', members: 13, score: 9410, volume: '244,100 kg', trend: '-2%' },
    { rank: 5, name: 'Old School Co.', members: 11, score: 8720, volume: '219,800 kg', trend: '+3%' },
  ];

  members = [
    { rank: 1, name: 'Dimitar', score: 766, contribution: '42,800 kg', badge: 'Captain' },
    { rank: 2, name: 'Alex', score: 741, contribution: '39,400 kg', badge: 'PR Hunter' },
    { rank: 3, name: 'Viktor', score: 709, contribution: '36,100 kg', badge: 'Volume' },
    { rank: 4, name: 'Mira', score: 694, contribution: '33,700 kg', badge: 'Streak' },
  ];

  challenges = [
    { name: 'Push Week', progress: 84, target: '250,000 kg clan volume' },
    { name: 'PR Ladder', progress: 62, target: '25 records logged' },
    { name: 'Full Attendance', progress: 71, target: '5 workouts each' },
  ];
}
