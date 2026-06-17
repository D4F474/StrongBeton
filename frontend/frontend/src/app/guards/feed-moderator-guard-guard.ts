import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';

import { AuthState } from '../common/user/auth-state';
import { AuthService } from '../services/auth-service';
import { userDto } from '../common/user/user-dto';

export const feedModeratorGuard: CanActivateFn = () => {
  const authState = inject(AuthState);
  const authService = inject(AuthService);
  const router = inject(Router);
  const currentUser = authState.user();

  if (canModerateFeed(currentUser)) {
    return true;
  }

  if (!authState.token()) {
    return router.createUrlTree(['/login']);
  }

  return authService.getMe().pipe(
    map((user) => {
      authState.setUser(user);

      return canModerateFeed(user)
        ? true
        : router.createUrlTree(['/app/home']);
    }),
    catchError(() => of(router.createUrlTree(['/app/home'])))
  );
};

function canModerateFeed(user: userDto | null): boolean {
  const role = user?.role?.toUpperCase();
  return role === 'FEED_MODERATOR' || role === 'OWNER';
}
