import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';

import { AuthService } from '../../../services/auth-service';
import { FriendsService } from '../../../services/friends-service';
import { UserStatusDto } from '../../../common/social/user-status-dto';
import { FriendViewDto } from '../../../common/social/friend-view-dto';

@Component({
  selector: 'app-friends',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './friends.html',
  styleUrl: './friends.scss',
})
export class Friends implements OnInit {
  username: string | null = null;

  suggestions: UserStatusDto[] = [];
  friends: FriendViewDto[] = [];
  private failedImageUrls = new Set<string>();

  pageReady = false;
  actionLoading = false;

  actionError: string | null = null;
  actionSuccess: string | null = null;

  constructor(
    private authService: AuthService,
    private friendsService: FriendsService,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCurrentUser();
  }

  get acceptedFriends(): FriendViewDto[] {
    return this.friends.filter((friend) => this.isAccepted(friend.status));
  }

  get incomingRequests(): FriendViewDto[] {
    return this.friends.filter((friend) => this.isIncomingRequest(friend.status));
  }

  get outgoingRequests(): FriendViewDto[] {
    return this.friends.filter((friend) => this.isOutgoingRequest(friend.status));
  }

  private loadCurrentUser(): void {
    this.pageReady = false;
    this.syncView();

    this.authService
      .getMe()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (user) => {
          this.username = user.username ?? user.email ?? null;
          this.loadSocialData();
        },
        error: (error) => {
          console.error('Failed to load current user:', error);
          this.actionError = 'Could not load current user.';
          this.pageReady = true;
          this.syncView();
        },
      });
  }

  loadSocialData(): void {
    this.loadFriends();
    this.loadSuggestions();
  }

  loadFriends(): void {
    if (!this.username) {
      this.friends = [];
      this.pageReady = true;
      this.syncView();
      return;
    }

    this.friendsService
      .getFriends(this.username)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        catchError((error) => {
          if (error.status !== 404) {
            console.error('Failed to load friends:', error);
          }

          return of([]);
        })
      )
      .subscribe({
        next: (friends) => {
          this.friends = friends ?? [];
          this.pruneFailedImageUrls();
          this.pageReady = true;
          this.syncView();
        },
      });
  }

  loadSuggestions(): void {
    this.friendsService
      .getSuggestedUsers()
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        catchError((error) => {
          if (error.status !== 404) {
            console.error('Failed to load suggestions:', error);
          }

          return of([]);
        })
      )
      .subscribe({
        next: (suggestions) => {
          this.suggestions = suggestions ?? [];
          this.pruneFailedImageUrls();
          this.syncView();
        },
      });
  }

  sendRequest(username: string): void {
    if (!username || this.actionLoading) {
      return;
    }

    this.runAction(
      this.friendsService.sendFriendRequest(username),
      'Friend request sent.'
    );
  }

  acceptRequest(username: string): void {
    if (!username || this.actionLoading) {
      return;
    }

    this.runAction(
      this.friendsService.acceptFriendRequest(username),
      'Friend request accepted.'
    );
  }

  declineRequest(username: string): void {
    if (!username || this.actionLoading) {
      return;
    }

    this.runAction(
      this.friendsService.declineFriendRequest(username),
      'Friend request declined.'
    );
  }

  removeFriend(username: string): void {
    if (!username || this.actionLoading) {
      return;
    }

    const confirmed = window.confirm('Remove this friend?');

    if (!confirmed) {
      return;
    }

    this.runAction(
      this.friendsService.removeFriend(username),
      'Friend removed.'
    );
  }

  private runAction(request$: any, successMessage: string): void {
    this.actionLoading = true;
    this.actionError = null;
    this.actionSuccess = null;
    this.syncView();

    request$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.actionLoading = false;
          this.actionSuccess = successMessage;

          this.loadSocialData();
          this.syncView();
        },
        error: (error: any) => {
          console.error('Friend action failed:', error);

          this.actionLoading = false;
          this.actionError =
            error?.error?.message ||
            error?.error?.error ||
            error?.error ||
            error?.message ||
            'Friend action failed.';

          this.syncView();
        },
      });
  }

  isAccepted(status: string | null | undefined): boolean {
    return String(status ?? '').toUpperCase().includes('ACCEPTED');
  }

  isIncomingRequest(status: string | null | undefined): boolean {
    return String(status ?? '').toUpperCase().includes('RESPONSE');
  }

  isOutgoingRequest(status: string | null | undefined): boolean {
    const value = String(status ?? '').toUpperCase();

    return (
      value.includes('REQUEST') ||
      value.includes('PENDING') ||
      value.includes('INVITE') ||
      value.includes('SENT')
    ) && !value.includes('RESPONSE');
  }

  trackByFriend(index: number, friend: FriendViewDto): string {
    return friend.friend ?? String(index);
  }

  trackByUsername(index: number, user: UserStatusDto): string {
    return user.username ?? String(index);
  }

  getProfileImageUrl(entity: FriendViewDto | UserStatusDto): string | null {
    const url = entity.profileImageUrl?.trim();

    if (!url || this.failedImageUrls.has(url)) {
      return null;
    }

    return url;
  }

  markImageFailed(url: string | null): void {
    if (url) {
      this.failedImageUrls.add(url);
      this.syncView();
    }
  }

  private syncView(): void {
    this.cdr.markForCheck();
  }

  getInitials(value: string | null | undefined): string {
    if (!value) {
      return '?';
    }

    return value
      .trim()
      .split(/\s+/)
      .map((part) => part[0])
      .join('')
      .slice(0, 2)
      .toUpperCase();
  }

  private pruneFailedImageUrls(): void {
    const visibleUrls = new Set(
      [...this.friends, ...this.suggestions]
        .map((item) => item.profileImageUrl?.trim())
        .filter((url): url is string => !!url)
    );

    this.failedImageUrls.forEach((url) => {
      if (!visibleUrls.has(url)) {
        this.failedImageUrls.delete(url);
      }
    });
  }
}
