import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { UserDetails } from '../common/user-details';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService  {

  constructor(private httpClient: HttpClient) { }

  private baseUrl = "http://localhost:8081/users";
  //private baseUrl = 'http://192.168.0.104:8081/users';

  getUser(token: string | null) : Observable<UserDetails> {
    const Url = `${this.baseUrl}/me`;

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
      
    return this.httpClient.get<UserDetails>(Url, { headers });
  }
  
  getUserInfo(username: string) :Observable<UserDetails> {
    const Url = `${this.baseUrl}/GetUser/${username}`;

    return this.httpClient.get<UserDetails>(Url);
  }

  

}

