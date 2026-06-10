import { Component, DestroyRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  ActiveWorkoutPreview,
  WorkoutService,
} from '../../../services/workout-service';
import { StatsService } from '../../../services/stats-service';
import { ChangeDetectorRef } from '@angular/core';

type DashboardStat = {
  label: string;
  value: string;
  helper: string;
  valueClass: string;
  helperClass: string;
};

type RecentWorkout = {
  day: string;
  title: string;
  volume: string;
  status: string;
  statusClass: string;
};

@Component({
  selector: 'app-home-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home-dashboard.html',
  styleUrl: './home-dashboard.scss',
})
export class HomeDashboard implements OnInit {
  // ── state subjects ──────────────────────────────────────────
  activeWorkout: ActiveWorkoutPreview | null = null;
  activeWorkoutLoaded = false;

  stats: DashboardStat[] = this.buildLoadingStats();
  recentWorkouts: RecentWorkout[] = [];
  statsLoaded = false;
  statsError = false;

dashboardReady = false;

  constructor(
  private workoutService: WorkoutService,
  private statsService: StatsService,
  private destroyRef: DestroyRef,
  private cdr: ChangeDetectorRef
) {}

  ngOnInit(): void {
  this.loadActiveWorkout();
  this.loadDashboardStats();
}

  private loadActiveWorkout(): void {
  this.workoutService
    .getActiveWorkoutPreview()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (activeWorkout) => {
        this.activeWorkout = activeWorkout;
        this.activeWorkoutLoaded = true;
        this.updateDashboardReady();
      },
      error: (error) => {
        console.error('Failed to load active workout:', error);

        this.activeWorkout = null;
        this.activeWorkoutLoaded = true;
        this.updateDashboardReady();
      },
    });
}

  get hasActiveWorkout(): boolean {
    return this.activeWorkout !== null;
  }

  get heroTitle(): string {
    return this.hasActiveWorkout ? 'Workout active.' : 'Push Day.';
  }

  get heroHighlight(): string {
    return this.hasActiveWorkout ? 'Finish the job.' : 'Start heavy.';
  }

  get heroDescription(): string {
    if (!this.activeWorkout) {
      return 'Last session is ready. Values repeat automatically. Change only what needs changing.';
    }
    return `${this.activeWorkout.name} is still active. Continue from where you stopped.`;
  }

  get primaryButtonText(): string {
    return this.hasActiveWorkout ? 'Continue Workout' : 'Train Now';
  }

  trackByIndex(index: number): number {
    return index;
  }

  get primaryWorkoutQueryParams(): { workoutId: string } | null {
    if (!this.activeWorkout) return null;
    return { workoutId: this.activeWorkout.id };
  }

  private loadDashboardStats(): void {
  this.statsService
    .getOverview()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (overview) => {
        this.stats = this.buildStats(overview);
        this.recentWorkouts = this.buildRecentRecords(
          overview.recentRecords ?? []
        );

        this.statsError = false;
        this.statsLoaded = true;
        this.updateDashboardReady();
      },
      error: (error) => {
        console.error('Failed to load dashboard stats:', error);

        this.stats = this.buildErrorStats();
        this.recentWorkouts = [];
        this.statsError = true;
        this.statsLoaded = true;
        this.updateDashboardReady();
      },
    });
}

private updateDashboardReady(): void {
  this.dashboardReady = this.activeWorkoutLoaded && this.statsLoaded;
  this.cdr.markForCheck();
}

  // ── builders (непроменени) ────────────────────────────────────
  private buildStats(overview: any): DashboardStat[] {
    const scoreDelta = overview.strengthScoreDelta ?? 0;
    const weeklyVolume = overview.weeklyVolume ?? 0;
    const weeklyVolumeTarget = overview.weeklyVolumeTarget ?? 0;
    const weeklyVolumePercent = overview.weeklyVolumePercent ?? 0;
    const trainingStreak = overview.trainingStreak ?? 0;
    const bestStreak = overview.bestStreak ?? 0;
    const personalRecords = overview.personalRecords ?? 0;
    const recordsThisMonth = overview.recordsThisMonth ?? 0;

    return [
      {
        label: 'Strength Score',
        value: this.formatNumber(overview.strengthScore ?? 0),
        helper: `${scoreDelta >= 0 ? '+' : ''}${scoreDelta} this month`,
        valueClass: 'text-[#F59E0B] text-3xl md:text-5xl',
        helperClass: scoreDelta >= 0 ? 'text-[#22C55E]' : 'text-red-500',
      },
      {
        label: 'Weekly Volume',
        value: this.formatNumber(weeklyVolume),
        helper: `${weeklyVolumePercent}% of ${this.formatNumber(weeklyVolumeTarget)} kg`,
        valueClass: 'text-[#172033] text-2xl md:text-4xl',
        helperClass: 'text-[#6B7280]',
      },
      {
        label: 'Training Streak',
        value: String(trainingStreak),
        helper: `best streak ${bestStreak}`,
        valueClass: 'text-[#F59E0B] text-3xl md:text-5xl',
        helperClass: 'text-[#6B7280]',
      },
      {
        label: 'Personal Records',
        value: String(personalRecords),
        helper: `${recordsThisMonth} this month`,
        valueClass: 'text-[#22C55E] text-3xl md:text-5xl',
        helperClass: 'text-[#6B7280]',
      },
    ];
  }

  private buildRecentRecords(records: any[]): RecentWorkout[] {
    return records.slice(0, 3).map((record) => ({
      day: this.formatDate(record.date),
      title: record.exerciseName ?? 'Exercise',
      volume: `${this.formatKg(record.kg)} x ${record.reps ?? 0}`,
      status: `e1RM ${this.formatKg(record.estimatedOneRepMax)}`,
      statusClass: 'text-[#22C55E]',
    }));
  }

  private buildLoadingStats(): DashboardStat[] {
    return [
      { label: 'Strength Score',   value: '...', helper: 'loading', valueClass: 'text-[#F59E0B] text-3xl md:text-5xl', helperClass: 'text-[#6B7280]' },
      { label: 'Weekly Volume',    value: '...', helper: 'loading', valueClass: 'text-[#172033] text-2xl md:text-4xl', helperClass: 'text-[#6B7280]' },
      { label: 'Training Streak',  value: '...', helper: 'loading', valueClass: 'text-[#F59E0B] text-3xl md:text-5xl', helperClass: 'text-[#6B7280]' },
      { label: 'Personal Records', value: '...', helper: 'loading', valueClass: 'text-[#22C55E] text-3xl md:text-5xl', helperClass: 'text-[#6B7280]' },
    ];
  }

  private buildErrorStats(): DashboardStat[] {
    return [
      { label: 'Strength Score',   value: '-', helper: 'failed to load', valueClass: 'text-[#F59E0B] text-3xl md:text-5xl', helperClass: 'text-red-500' },
      { label: 'Weekly Volume',    value: '-', helper: 'failed to load', valueClass: 'text-[#172033] text-2xl md:text-4xl', helperClass: 'text-red-500' },
      { label: 'Training Streak',  value: '-', helper: 'failed to load', valueClass: 'text-[#F59E0B] text-3xl md:text-5xl', helperClass: 'text-red-500' },
      { label: 'Personal Records', value: '-', helper: 'failed to load', valueClass: 'text-[#22C55E] text-3xl md:text-5xl', helperClass: 'text-red-500' },
    ];
  }

  private formatNumber(value: number): string {
    return Math.round(value).toLocaleString('en-US');
  }

  private formatKg(value: number): string {
    const rounded = Math.round((value ?? 0) * 10) / 10;
    return `${rounded.toLocaleString('en-US')}kg`;
  }

  private formatDate(value: string): string {
    if (!value) return 'RECENT';
    return new Date(value).toLocaleDateString('en-US', { weekday: 'short' });
  }
}