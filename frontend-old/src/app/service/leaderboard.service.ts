import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Leaderboard } from '../common/leaderboard';

@Injectable({
  providedIn: 'root'
})
export class LeaderboardService {

  private baseUrl = 'http://localhost:8081/api';
  //private baseUrl = 'http://192.168.0.104:8081/api';
    constructor(private httpClient: HttpClient) { }

    getLeaderBoard() : Observable<Leaderboard[]>{
      const url = this.baseUrl + "/leaderBoard";
        return this.httpClient.get<Leaderboard[]>(url);
    }
}
