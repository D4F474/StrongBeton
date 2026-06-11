import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, DestroyRef, Input, OnChanges } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { ClanService } from '../../../../../services/clan-service';
import { ClanMemberContributionDto } from '../../../../../common/clan/clan-member-contribution-dto';

@Component({
  selector: 'app-clan-activity',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './clan-activity.html',
  styleUrl: './clan-activity.scss',
})
export class ClanActivity implements OnChanges {
  @Input() clanId: number | null = null;

  contributions: ClanMemberContributionDto[] = [];
  loaded = false;
  error: string | null = null;

  constructor(
    private clanService: ClanService,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnChanges(): void {
    if (!this.clanId) {
      return;
    }

    this.loadActivity();
  }

  get hasActivity(): boolean {
    return this.contributions.length > 0;
  }

  loadActivity(): void {
    if (!this.clanId) {
      return;
    }

    this.loaded = false;
    this.error = null;
    this.syncView();

    this.clanService
      .getClanContributions(this.clanId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (contributions) => {
          this.contributions = contributions ?? [];
          this.loaded = true;
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to load clan activity:', error);
          this.contributions = [];
          this.error = 'Could not load clan activity.';
          this.loaded = true;
          this.syncView();
        },
      });
  }

  formatDate(value: string): string {
    if (!value) {
      return 'recent';
    }

    return new Date(value).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
    });
  }

  trackByIndex(index: number): number {
    return index;
  }

  private syncView(): void {
    this.cdr.markForCheck();
  }
}