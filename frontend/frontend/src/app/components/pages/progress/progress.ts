import { Component, DestroyRef, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { StatsService } from '../../../services/stats-service';
import { StatsOverviewDto } from '../../../common/stats/stats-overview-dto';

type LoadState =
  | { status: 'loading' }
  | { status: 'success'; data: StatsOverviewDto }
  | { status: 'error'; message: string };

@Component({
  selector: 'app-progress',
  imports: [CommonModule],
  templateUrl: './progress.html',
  styleUrl: './progress.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Progress implements OnInit {
  state: LoadState = { status: 'loading' };

  constructor(
    private statsService: StatsService,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  private loadStats(): void {
    this.state = { status: 'loading' };
    this.cdr.markForCheck();

    this.statsService
      .getOverview()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (data) => {
          this.state = { status: 'success', data };
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error(err);
          this.state = { status: 'error', message: 'Не успяхме да заредим статистиките.' };
          this.cdr.markForCheck();
        },
      });
  }

  get maxWeeklyVolume(): number {
    if (this.state.status !== 'success') return 1;
    const bars = this.state.data.weeklyVolumeBars ?? [];
    return Math.max(...bars.map((b) => b.volume), 1);
  }

  getVolumePercent(volume: number): number {
    if (!volume || volume <= 0) return 0;
    return Math.round((volume / this.maxWeeklyVolume) * 100);
  }

  get weightHistory() {
    if (this.state.status !== 'success') return [];
    return this.state.data.weightHistory ?? [];
  }

  get currentWeight(): number | null {
    const history = this.weightHistory;
    if (history.length === 0) return null;
    return history[history.length - 1].kg;
  }

  get weightChange(): number {
    const history = this.weightHistory;
    if (history.length < 2) return 0;
    return Math.round((history[history.length - 1].kg - history[0].kg) * 10) / 10;
  }

  get minWeight(): number {
    const history = this.weightHistory;
    if (history.length === 0) return 0;
    return Math.min(...history.map((item) => item.kg));
  }

  get maxWeight(): number {
    const history = this.weightHistory;
    if (history.length === 0) return 1;
    return Math.max(...history.map((item) => item.kg));
  }

  getWeightPointTop(kg: number): number {
    const range = this.maxWeight - this.minWeight;
    if (range <= 0) return 50;
    return 88 - ((kg - this.minWeight) / range) * 76;
  }
}
