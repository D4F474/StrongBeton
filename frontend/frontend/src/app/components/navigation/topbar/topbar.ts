import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { AuthService } from '../../../services/auth-service';
import { AuthState } from '../../../common/user/auth-state';
import { userDto } from '../../../common/user/user-dto';

@Component({
  selector: 'app-topbar',
  imports: [CommonModule, RouterLink],
  templateUrl: './topbar.html',
  styleUrl: './topbar.scss',
})
export class Topbar implements OnInit {
  logoAvailable = true;
  user: userDto | null = null;

  constructor(
    private authService: AuthService,
    private authState: AuthState,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUser();
  }

  get canModerateFeed(): boolean {
    const role = this.user?.role?.toUpperCase();
    return role === 'FEED_MODERATOR' || role === 'OWNER';
  }

  hideLogo(): void {
    this.logoAvailable = false;
  }

  logout(): void {
  this.authService.logout();
}

  private loadUser(): void {
    const cachedUser = this.authState.user();

    if (cachedUser) {
      this.user = cachedUser;
      this.cdr.markForCheck();
      return;
    }

    if (!this.authState.token()) {
      return;
    }

    this.authService
      .getMe()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (user) => {
          this.user = user;
          this.authState.setUser(user);
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Failed to load topbar user:', error);
          this.user = null;
          this.cdr.markForCheck();
        },
      });
  }
}
