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
    return this.hasActiveWorkout ? 'Доизбутай деня.' : 'Активна тренировка.';
  }

  get heroHighlight(): string {
    return this.hasActiveWorkout ? 'Довърши започнатото.' : 'Започни тежко.';
  }

  get heroDescription(): string {
    if (!this.activeWorkout) {
      return 'Последната тренировка е заредена. Стойностите се повтарят автоматично. Промени само това, което трябва.';
    }
    return `${this.activeWorkout.name} все още е активна. Продължи оттам, докъдето стигна.`;
  }

  get primaryButtonText(): string {
    return this.hasActiveWorkout ? 'Продължи тренировката' : 'Тренирай сега';
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
      label: 'Сила',
      value: this.formatNumber(overview.strengthScore ?? 0),
      helper: `${scoreDelta >= 0 ? '+' : ''}${scoreDelta} този месец`,
      valueClass: 'text-[#F59E0B] text-3xl md:text-5xl',
      helperClass: scoreDelta >= 0 ? 'text-[#22C55E]' : 'text-red-500',
    },
    {
      label: 'Седмичен обем',
      value: this.formatNumber(weeklyVolume),
      helper: `${weeklyVolumePercent}% от ${this.formatNumber(weeklyVolumeTarget)} кг`,
      valueClass: 'text-[#172033] text-2xl md:text-4xl',
      helperClass: 'text-[#6B7280]',
    },
    {
      label: 'Поредица от тренировки',
      value: String(trainingStreak),
      helper: `най-добра поредица ${bestStreak}`,
      valueClass: 'text-[#F59E0B] text-3xl md:text-5xl',
      helperClass: 'text-[#6B7280]',
    },
    {
      label: 'Лични рекорди',
      value: String(personalRecords),
      helper: `${recordsThisMonth} този месец`,
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
      { label: 'Сила',   value: '...', helper: 'loading', valueClass: 'text-[#F59E0B] text-3xl md:text-5xl', helperClass: 'text-[#6B7280]' },
      { label: 'Седмичен обем',    value: '...', helper: 'loading', valueClass: 'text-[#172033] text-2xl md:text-4xl', helperClass: 'text-[#6B7280]' },
      { label: 'Поредица от тренировки',  value: '...', helper: 'loading', valueClass: 'text-[#F59E0B] text-3xl md:text-5xl', helperClass: 'text-[#6B7280]' },
      { label: 'Лични рекорди', value: '...', helper: 'loading', valueClass: 'text-[#22C55E] text-3xl md:text-5xl', helperClass: 'text-[#6B7280]' },
    ];
  }

  private buildErrorStats(): DashboardStat[] {
    return [
      { label: 'Сила',   value: '-', helper: 'failed to load', valueClass: 'text-[#F59E0B] text-3xl md:text-5xl', helperClass: 'text-red-500' },
      { label: 'Седмичен обем',    value: '-', helper: 'failed to load', valueClass: 'text-[#172033] text-2xl md:text-4xl', helperClass: 'text-red-500' },
      { label: 'Поредица от тренировки',  value: '-', helper: 'failed to load', valueClass: 'text-[#F59E0B] text-3xl md:text-5xl', helperClass: 'text-red-500' },
      { label: 'Лични рекорди', value: '-', helper: 'failed to load', valueClass: 'text-[#22C55E] text-3xl md:text-5xl', helperClass: 'text-red-500' },
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