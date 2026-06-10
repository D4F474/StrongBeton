import { computed, Injectable, signal } from '@angular/core';
import { userDto } from './user-dto';

@Injectable({ providedIn: 'root' })
export class AuthState {
  private readonly tokenKey = 'strongbeton_token';

  private readonly _user = signal<userDto | null>(null);

  private readonly _token = signal<string | null>(
    localStorage.getItem(this.tokenKey)
  );

  readonly user = this._user.asReadonly();
  readonly token = this._token.asReadonly();

  readonly isLoggedIn = computed(() => this._token() !== null);

  setUser(user: userDto | null): void {
    this._user.set(user);
  }

  setToken(token: string | null): void {
    if (token) {
      localStorage.setItem(this.tokenKey, token);
    } else {
      localStorage.removeItem(this.tokenKey);
    }

    this._token.set(token);
  }

  clear(): void {
    localStorage.removeItem(this.tokenKey);
    this._user.set(null);
    this._token.set(null);
  }
}
