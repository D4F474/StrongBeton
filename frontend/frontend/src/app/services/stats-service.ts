import { Injectable } from '@angular/core';
import { StatsOverviewDto } from '../common/stats/stats-overview-dto';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class StatsService {
  private readonly baseUrl = 'http://localhost:8081/api/stats';

  constructor(private httpClient: HttpClient) {}

  getOverview(): Observable<StatsOverviewDto> {
    return this.httpClient.get<StatsOverviewDto>(`${this.baseUrl}/overview`);
  }
}
