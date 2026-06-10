import { Component, OnInit, DestroyRef, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ImageService } from '../../../services/image-service';
import { ProfileImageData } from '../../../common/image/profile-image-data';
import { AuthService } from '../../../services/auth-service';
import { StatsService } from '../../../services/stats-service';
import { userDto } from '../../../common/user/user-dto';
import { AuthState } from '../../../common/user/auth-state';

type OverviewStats = {
  score: number;
  streak: number;
};

type LoadState =
  | { status: 'loading' }
  | { status: 'success'; data: OverviewStats }
  | { status: 'error'; message: string };

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar implements OnInit {
  state: LoadState = { status: 'loading' };

  user: userDto | null = null;
  userLoaded = false;

  image: ProfileImageData | null = null;
  photoPreviewUrl: string | null = null;
  profileImageFailed = false;
  profileImageLoaded = false;

  constructor(
    private authService: AuthService,
    private statsService: StatsService,
    private imageService: ImageService,
    private authState: AuthState,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUser();
    this.loadStats();
    this.loadProfileImage();
  }

  get displayName(): string {
    if (!this.userLoaded) {
      return 'Loading';
    }

    return this.user?.username ?? this.user?.email ?? 'Athlete';
  }

  get hasProfileImage(): boolean {
    return !!this.photoPreviewUrl && !this.profileImageFailed;
  }

  get statsLoading(): boolean {
    return this.state.status === 'loading';
  }

  get score(): number | string {
    if (this.state.status !== 'success') {
      return '—';
    }

    return this.state.data.score;
  }

  get streak(): number | string {
    if (this.state.status !== 'success') {
      return '—';
    }

    return this.state.data.streak;
  }

  private loadUser(): void {
    const cachedUser = this.authState.user() ?? null;

    if (cachedUser) {
      this.user = cachedUser;
      this.userLoaded = true;
      this.cdr.markForCheck();
      return;
    }

    this.authService
      .getMe()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (user) => {
          this.user = user;
          this.userLoaded = true;
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Failed to load sidebar user:', error);

          this.user = null;
          this.userLoaded = true;
          this.cdr.markForCheck();
        },
      });
  }

  private loadStats(): void {
    this.state = { status: 'loading' };
    this.cdr.markForCheck();

    this.statsService
      .getOverview()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (raw: any) => {
          this.state = {
            status: 'success',
            data: {
              score: Number(
                raw?.score ??
                  raw?.strengthScore ??
                  raw?.strength_score ??
                  raw?.totalScore ??
                  0
              ),
              streak: Number(
                raw?.streak ??
                  raw?.trainingStreak ??
                  raw?.training_streak ??
                  raw?.trainingStreakDays ??
                  0
              ),
            },
          };

          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Failed to load sidebar stats:', err);

          this.state = {
            status: 'error',
            message: 'Не успяхме да заредим статистиките.',
          };

          this.cdr.markForCheck();
        },
      });
  }

  private loadProfileImage(): void {
    this.profileImageLoaded = false;

    this.imageService
      .getProfileImage()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (image: ProfileImageData) => {
          this.image = image;
          this.photoPreviewUrl = image.photoUrl;
          this.profileImageFailed = false;
          this.profileImageLoaded = true;

          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Failed to load sidebar profile image:', err);

          this.image = null;
          this.photoPreviewUrl = null;
          this.profileImageFailed = true;
          this.profileImageLoaded = true;

          this.cdr.markForCheck();
        },
      });
  }

  hideProfilePhoto(): void {
    this.profileImageFailed = true;
    this.cdr.markForCheck();
  }

  logout(): void {
    this.authService.logout();
  }
}