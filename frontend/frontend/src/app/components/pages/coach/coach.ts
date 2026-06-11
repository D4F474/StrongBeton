import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-coach',
  imports: [CommonModule],
  templateUrl: './coach.html',
  styleUrl: './coach.scss',
})
export class Coach {
  clients = [
    { name: 'Dimitar', plan: 'Push/Pull/Legs', adherence: 92, status: 'Ready', score: 766 },
    { name: 'Mira', plan: 'Strength Base', adherence: 86, status: 'Needs review', score: 694 },
    { name: 'Alex', plan: 'Hypertrophy Block', adherence: 78, status: 'Missed check-in', score: 741 },
    { name: 'Viktor', plan: 'Power Phase', adherence: 95, status: 'Ready', score: 709 },
  ];

  tasks = [
    { title: 'Review Mira squat videos', meta: 'Technique feedback due today' },
    { title: 'Adjust Alex week 4 volume', meta: 'Elbow fatigue reported' },
    { title: 'Approve Dimitar bench PR', meta: '120kg x 5 submitted' },
  ];

  signals = [
    { label: 'Active Clients', value: '24', sublabel: '+3 this month' },
    { label: 'Check-ins Due', value: '7', sublabel: '3 urgent' },
    { label: 'Avg Adherence', value: '88%', sublabel: '+5% vs last block' },
    { label: 'PRs Logged', value: '31', sublabel: 'This week' },
  ];
}
