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

  currentUserId: string | null = null;
  currentUsername: string | null = null;

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
    return this.myClan?.members?.length ?? 0;
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
        },
        error: (error) => {
          console.error('Failed to load my clan:', error);
          this.myClan = null;
          this.myClanLoaded = true;
          this.updatePageReady();
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
        this.currentUserId = user.id;
        this.currentUsername = user.username ?? user.email ?? null;
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to load current user:', error);
        this.currentUserId = null;
        this.currentUsername = null;
        this.syncView();
      },
    });
}

get isCurrentUserLeader(): boolean {
  if (!this.currentUsername || !this.myClan?.members) {
    return false;
  }

  return this.myClan.members.some((member) => {
    const memberName =
      member.username ||
      member.user?.username ||
      member.user?.email;

    return (
      memberName === this.currentUsername &&
      String(member.clanRoleType ?? '').toUpperCase() === 'LEADER'
    );
  });
}

openSettings(): void {
  if (!this.isCurrentUserLeader) {
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
  if (!this.myClan?.id || !this.currentUserId || this.actionLoading) {
    return;
  }

  this.actionLoading = true;
  this.actionError = null;
  this.actionSuccess = null;
  this.syncView();

  this.clanService
    .updateClan(this.myClan.id, this.currentUserId, payload)
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
}
