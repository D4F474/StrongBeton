import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClanDto } from '../../../../../common/clan/clan-dto';

@Component({
  selector: 'app-clan-overview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './clan-overview.html',
  styleUrl: './clan-overview.scss',
})
export class ClanOverview {
  @Input() clan: ClanDto | null = null;

  get clanPoints(): number {
    return this.clan?.clanPoints ?? 0;
  }

  get membersCount(): number {
    return this.clan?.members?.length ?? 0;
  }

  get clanLeague(): string {
    return this.clan?.currLeague ?? 'UNRANKED';
  }

  get inviteMode(): string {
    const inviteOnly = this.clan?.invite ?? this.clan?.isInvite ?? false;
    return inviteOnly ? 'Invite only' : 'Open clan';
  }
}