import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClanLeaderboardDto } from '../../../../../common/clan/clan-leaderboard-dto';

@Component({
  selector: 'app-clan-leaderboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './clan-leaderboard.html',
  styleUrl: './clan-leaderboard.scss',
})
export class ClanLeaderboard {
  @Input() topClans: ClanLeaderboardDto[] = [];
  @Input() hasClan = false;
  @Input() actionLoading = false;

  @Output() joinClanRequested = new EventEmitter<number>();

  joinClan(clanId: number | null): void {
    if (!clanId || this.actionLoading) {
      return;
    }

    this.joinClanRequested.emit(clanId);
  }

  trackByIndex(index: number): number {
    return index;
  }
}