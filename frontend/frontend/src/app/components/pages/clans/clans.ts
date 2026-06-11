import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ClanMemberDto } from '../../../common/clan/clan-member-dto';
import { ClanOverview } from './components/clan-overview/clan-overview';
import { ClanMembers } from './components/clan-members/clan-members';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth-service';

import { ClanEmptyState } from './components/clan-empty-state/clan-empty-state';
import { ClanLeaderboard } from './components/clan-leaderboard/clan-leaderboard';
import { CreateClanPayload } from '../../../common/clan/create-clan-payload';
import { ClanActivity } from './components/clan-activity/clan-activity';

import { ClanService } from '../../../services/clan-service';
import { ClanDto } from '../../../common/clan/clan-dto';
import { ClanLeaderboardDto } from '../../../common/clan/clan-leaderboard-dto';
import { ClanSettings } from './components/clan-settings/clan-settings';
import { UpdateClanPayLoad } from '../../../common/clan/update-clan-pay-load';

@Component({
  selector: 'app-clans',
  imports: [
  CommonModule,
  ClanEmptyState,
  ClanLeaderboard,
  ClanOverview,
  ClanMembers,
  ClanActivity,
  ClanSettings
],
  templateUrl: './clans.html',
  styleUrl: './clans.scss',
})
export class Clans {
  myClan: ClanDto | null = null;
  topClans: ClanLeaderboardDto[] = [];
  pendingRequests: ClanMemberDto[] = [];

  myClanLoaded = false;
  leaderboardLoaded = false;
  pageReady = false;

  createName = '';
  createDescription = '';
  createInviteOnly = false;

  joinClanId: number | null = null;

  actionLoading = false;
  actionError: string | null = null;
  actionSuccess: string | null = null;

  settingsOpen = false;

  currentUserUuid: string | null = null;

  constructor(
    private clanService: ClanService,
    private destroyRef: DestroyRef,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCurrentUser();
    this.loadMyClan();
    this.loadTopClans();
  }

  get hasClan(): boolean {
    return this.myClan !== null;
  }

  get membersCount(): number {
  return (this.myClan?.members ?? []).filter((member) => {
    const role = String(member.clanRoleType ?? '').toUpperCase();
    return !role.includes('PENDING');
  }).length;
}

  get clanPoints(): number {
    return this.myClan?.clanPoints ?? 0;
  }

  get clanLeague(): string {
    return this.myClan?.currLeague ?? 'UNRANKED';
  }

  get inviteMode(): string {
    const inviteOnly = this.myClan?.invite ?? this.myClan?.isInvite ?? false;
    return inviteOnly ? 'Invite only' : 'Open clan';
  }

  get sortedMembers() {
    return [...(this.myClan?.members ?? [])].sort(
      (a, b) => (b.points ?? 0) - (a.points ?? 0)
    );
  }

  loadMyClan(): void {
    this.myClanLoaded = false;
    this.updatePageReady();

    this.clanService
      .getMyClan()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (clan) => {
      this.myClan = clan;
      this.myClanLoaded = true;
      this.updatePageReady();


      if (this.isCurrentUserLeader && this.myClan?.id) {
        this.loadPendingRequests();
      } else {
        this.pendingRequests = [];
      }

      this.syncView();
    },
      });
  }

  loadTopClans(): void {
    this.leaderboardLoaded = false;
    this.updatePageReady();

    this.clanService
      .getTopClans()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (clans) => {
          this.topClans = clans ?? [];
          this.leaderboardLoaded = true;
          this.updatePageReady();
        },
        error: (error) => {
          console.error('Failed to load top clans:', error);
          this.topClans = [];
          this.leaderboardLoaded = true;
          this.updatePageReady();
        },
      });
  }

  createClan(payload: CreateClanPayload): void {
  const name = payload.name.trim();

  if (!name || this.actionLoading) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .createClan({
      name,
      description: payload.description?.trim() ?? '',
      invite: payload.invite ?? false,
    })
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (createdClan) => {
        this.myClan = createdClan;

        this.actionLoading = false;
        this.actionSuccess = 'Clan created.';

        this.loadMyClan();
        this.loadTopClans();
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to create clan:', error);

        this.actionLoading = false;
        this.actionError =
          error?.error?.message ||
          error?.error?.error ||
          error?.message ||
          'Could not create clan.';

        this.syncView();
      },
    });
}

joinClan(clanId: number): void {
  if (!clanId || this.actionLoading) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .joinClan(clanId)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: () => {
        this.actionLoading = false;
        this.actionSuccess = 'Clan joined.';

        this.loadMyClan();
        this.loadTopClans();
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to join clan:', error);

        this.actionLoading = false;
        this.actionError =
          error?.error?.message ||
          error?.error?.error ||
          error?.message ||
          'Could not join clan.';

        this.syncView();
      },
    });
  }

  trackByIndex(index: number): number {
    return index;
  }

  private updatePageReady(): void {
    this.pageReady = this.myClanLoaded && this.leaderboardLoaded;
    this.syncView();
  }

  private syncView(): void {
    this.cdr.markForCheck();
  }

  getMemberName(member: ClanMemberDto): string {
  return (
    member.username ||
    member.user?.username ||
    member.user?.email ||
    'Athlete'
  );
}

leaveClan(): void {
  if (!this.myClan?.id || this.actionLoading) {
    return;
  }

  const confirmed = window.confirm(
    'Are you sure you want to leave this clan?'
  );

  if (!confirmed) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .leaveClan(this.myClan.id)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: () => {
        this.myClan = null;
        this.actionLoading = false;
        this.actionSuccess = 'You left the clan.';

        this.loadTopClans();
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to leave clan:', error);

        this.actionLoading = false;
        this.actionError =
          error?.error?.message ||
          error?.error?.error ||
          error?.error ||
          error?.message ||
          'Could not leave clan.';

        this.syncView();
      },
    });
}

private loadCurrentUser(): void {
  this.authService
    .getMe()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (user) => {

        this.currentUserUuid = user.id ?? null;

        this.syncView();
      },
      error: (error) => {
        console.error('Failed to load current user:', error);
        this.currentUserUuid = null;
        this.syncView();
      },
    });
}

get isCurrentUserLeader(): boolean {
  return String(this.currentUserClanRole ?? '')
    .toUpperCase()
    .includes('LEADER');
}

openSettings(): void {
  if (!this.hasClan) {
    return;
  }

  this.settingsOpen = true;
  this.syncView();
}

closeSettings(): void {
  if (this.actionLoading) {
    return;
  }

  this.settingsOpen = false;
  this.syncView();
}

updateClanSettings(payload: UpdateClanPayLoad): void {
  if (!this.myClan?.id || this.actionLoading) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .updateClan(this.myClan.id, payload)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (updatedClan) => {
        this.myClan = updatedClan;
        this.actionLoading = false;
        this.settingsOpen = false;
        this.actionSuccess = 'Clan settings updated.';

        this.loadMyClan();
        this.loadTopClans();
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to update clan:', error);

        this.actionLoading = false;
        this.actionError =
          error?.error?.message ||
          error?.error?.error ||
          error?.error ||
          error?.message ||
          'Could not update clan settings.';

        this.syncView();
      },
    });
}

get currentUserClanRole(): string | null {
  return this.myClan?.currentUserRole ?? null;
}

get isCurrentUserPending(): boolean {
  return String(this.currentUserClanRole ?? '')
    .toUpperCase()
    .includes('PENDING');
}

loadPendingRequests(): void {
  if (!this.myClan?.id || !this.isCurrentUserLeader) {
    this.pendingRequests = [];
    this.syncView();
    return;
  }

  this.clanService
    .getPendingRequests(this.myClan.id)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (requests) => {

        this.pendingRequests = requests ?? [];
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to load pending requests:', error);

        this.pendingRequests = [];
        this.syncView();
      },
    });
}

acceptPendingMember(targetUserUuid: string | null | undefined): void {
  if (!this.myClan?.id || !targetUserUuid || this.actionLoading) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .acceptPendingMember(this.myClan.id, targetUserUuid)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: () => {
        this.actionLoading = false;
        this.actionSuccess = 'Pending member accepted.';

        this.loadMyClan();
        this.loadTopClans();
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to accept pending member:', error);

        this.actionLoading = false;
        this.actionError =
          error?.error?.message ||
          error?.error?.error ||
          error?.error ||
          error?.message ||
          'Could not accept pending member.';

        this.syncView();
      },
    });
}

declinePendingMember(targetUserUuid: string | null | undefined): void {
  if (!this.myClan?.id || !targetUserUuid || this.actionLoading) {
    return;
  }

  const confirmed = window.confirm('Decline this join request?');

  if (!confirmed) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .declinePendingMember(this.myClan.id, targetUserUuid)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: () => {
        this.actionLoading = false;
        this.actionSuccess = 'Pending request declined.';

        this.loadMyClan();
        this.loadTopClans();
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to decline pending member:', error);

        this.actionLoading = false;
        this.actionError =
          error?.error?.message ||
          error?.error?.error ||
          error?.error ||
          error?.message ||
          'Could not decline pending request.';

        this.syncView();
      },
    });
}

get manageableMembers(): ClanMemberDto[] {
  return (this.myClan?.members ?? []).filter((member) => {
    const role = String(member.clanRoleType ?? '').toUpperCase();

    return (
      !!member.userUuid &&
      !role.includes('LEADER') &&
      !role.includes('PENDING')
    );
  });
}

kickMember(targetUserUuid: string | null | undefined): void {
  if (!this.myClan?.id || !targetUserUuid || this.actionLoading) {
    return;
  }

  const confirmed = window.confirm('Kick this member from the clan?');

  if (!confirmed) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .kickMember(this.myClan.id, targetUserUuid)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: () => {
        this.actionLoading = false;
        this.actionSuccess = 'Member kicked from clan.';

        this.loadMyClan();
        this.loadTopClans();
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to kick member:', error);

        this.actionLoading = false;
        this.actionError =
          error?.error?.message ||
          error?.error?.error ||
          error?.error ||
          error?.message ||
          'Could not kick member.';

        this.syncView();
      },
    });
}

transferLeadership(targetUserUuid: string | null | undefined): void {
  if (!this.myClan?.id || !targetUserUuid || this.actionLoading) {
    return;
  }

  const confirmed = window.confirm(
    'Transfer leadership to this member? You will become an officer.'
  );

  if (!confirmed) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .transferLeadership(this.myClan.id, targetUserUuid)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: () => {
        this.actionLoading = false;
        this.actionSuccess = 'Leadership transferred.';

        this.loadMyClan();
        this.loadTopClans();
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to transfer leadership:', error);

        this.actionLoading = false;
        this.actionError =
          error?.error?.message ||
          error?.error?.error ||
          error?.error ||
          error?.message ||
          'Could not transfer leadership.';

        this.syncView();
      },
    });
}

}
