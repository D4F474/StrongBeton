import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, Observable, of, tap } from 'rxjs';

import { AuthState } from '../common/user/auth-state';
import { AuthToken } from '../common/user/auth-token';
import { LoginDto } from '../common/user/login-dto';
import { RegisterDto } from '../common/user/register-dto';
import { userDto } from '../common/user/user-dto';
import { ClanDto } from '../common/clan/clan-dto';


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly authUrl = '/auth';
  private readonly usersUrl = '/users';

  constructor(
    private httpClient: HttpClient,
    private router: Router,
    private authState: AuthState
  ) {}

  register(registerJSON: RegisterDto): Observable<RegisterDto> {
    return this.httpClient.post<RegisterDto>(
      `${this.authUrl}/signup`,
      registerJSON
    );
  }

  login(loginJSON: LoginDto): Observable<AuthToken> {
    return this.httpClient.post<AuthToken>(
      `${this.authUrl}/login`,
      loginJSON
    ).pipe(
      tap((res) => {
        this.authState.setToken(res.token);
        this.authState.setUser(res.userDTO);
      })
    );
  }

  getMe(): Observable<userDto> {
    return this.httpClient.get<userDto>(`${this.usersUrl}/me`);
  }

  restoreSession(): Observable<userDto | null> {
    if (!this.authState.token()) {
      return of(null);
    }

    return this.getMe().pipe(
      tap((user) => {
        this.authState.setUser(user);
      }),
      catchError(() => {
        this.clearSession();
        return of(null);
      })
    );
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  clearSession(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('authTokenExpiry');
    this.authState.clear();
  }

  updateProfile(updatedUser: Partial<userDto>): Observable<userDto> {
  return this.httpClient.put<userDto>(
    `${this.usersUrl}/updateUserData`,
    updatedUser
  );
}

 
}
