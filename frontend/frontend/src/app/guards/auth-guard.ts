import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { AuthState } from '../common/user/auth-state';

export const authGuard: CanActivateFn = () => {
  const authState = inject(AuthState);
  const router = inject(Router);

  if (authState.isLoggedIn()) {
    return true;
  }

  return router.createUrlTree(['/login']);
};