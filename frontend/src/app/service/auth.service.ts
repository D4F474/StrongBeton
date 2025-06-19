import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterDTO } from '../common/register-dto';
import { LoginDto } from '../common/login-dto';
import { Observable } from 'rxjs';
import { AuthToken } from '../common/auth-token';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) { }

  private baseUrl = "http://localhost:8081/auth";

  register(registerJSON: RegisterDTO): Observable<RegisterDTO>{
    const Url = `${this.baseUrl}/signup`;
    
    return this.httpClient.post<RegisterDTO>(Url, registerJSON);
  }

  login(loginJSON: LoginDto): Observable<AuthToken> {
    const Url = `${this.baseUrl}/login`
      
    return this.httpClient.post<AuthToken>(Url, loginJSON);
  }

  isAuthenticated(): boolean{
    const token = localStorage.getItem("authToken");
    const expiry = Number(localStorage.getItem('authTokenExpiry'));
    return token != null && expiry > Date.now();
  }

  
  logout(): void {
    localStorage.clear();
  }

  storeToken(token: AuthToken): void {
    localStorage.setItem('authToken', token.token);
    localStorage.setItem('authTokenExpiry', (Date.now() + token.expiresIn).toString());
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  getTokenExpiry(): number | null {
    const expiry = localStorage.getItem('authTokenExpiry');
    return expiry ? parseInt(expiry, 10) : null;
  }

  isTokenExpired(): boolean {
    const expiry = this.getTokenExpiry();
    return expiry ? Date.now() > expiry : true;
  }


}
