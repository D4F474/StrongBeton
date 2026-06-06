import { Injectable, signal } from '@angular/core';
import { UserDetails } from '../common/user-details';

@Injectable({ providedIn: 'root' })
export class AuthState {
    user = signal<UserDetails | null>(null);
    token = signal<string | null>(null);

  setUser(u: UserDetails) {
    this.user.set(u);
  }

  setToken(t: string) {
    this.token.set(t);
  }
    
}
