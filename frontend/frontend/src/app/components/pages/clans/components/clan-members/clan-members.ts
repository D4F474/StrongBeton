import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { ClanDto } from '../../../../../common/clan/clan-dto';
import { ClanMemberDto } from '../../../../../common/clan/clan-member-dto';

@Component({
  selector: 'app-clan-members',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './clan-members.html',
  styleUrl: './clan-members.scss',
})
export class ClanMembers {
  @Input() clan: ClanDto | null = null;
  @Input() actionLoading = false;

  @Output() leaveClanRequested = new EventEmitter<void>();

  get sortedMembers(): ClanMemberDto[] {
    return [...(this.clan?.members ?? [])].sort(
      (a, b) => (b.points ?? 0) - (a.points ?? 0)
    );
  }

  getMemberName(member: ClanMemberDto): string {
    return (
      member.username ||
      member.user?.username ||
      member.user?.email ||
      'Athlete'
    );
  }

  getMemberRole(member: ClanMemberDto): string {
    return member.clanRoleType || 'MEMBER';
  }

  getMemberPoints(member: ClanMemberDto): number {
    return member.points ?? 0;
  }

  requestLeaveClan(): void {
    if (this.actionLoading) {
      return;
    }

    this.leaveClanRequested.emit();
  }

  trackByIndex(index: number): number {
    return index;
  }

  private isAcceptedMember(member: ClanMemberDto): boolean {
  const role = String(member.clanRoleType ?? '').toUpperCase();

  return !role.includes('PENDING');
}
}