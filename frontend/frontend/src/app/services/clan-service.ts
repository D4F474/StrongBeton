import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ClanDto } from '../common/clan/clan-dto';
import { Observable, of } from 'rxjs';
import { CreateClanPayload } from '../common/clan/create-clan-payload';
import { ClanLeaderboardDto } from '../common/clan/clan-leaderboard-dto';
import { catchError } from 'rxjs';
import { ClanMemberContributionDto } from '../common/clan/clan-member-contribution-dto';
import { UpdateClanPayLoad } from '../common/clan/update-clan-pay-load';
import { ClanMemberDto } from '../common/clan/clan-member-dto';

@Injectable({
  providedIn: 'root',
})
export class ClanService {
   private readonly baseUrl = '/api/clans';

  constructor(private httpClient: HttpClient) {}

  getMyClan(): Observable<ClanDto | null> {
    return this.httpClient.get<ClanDto>(`${this.baseUrl}/me`).pipe(
      catchError((error) => {
        if (error.status === 404 || error.status === 204) {
          return of(null);
        }

        console.error('Failed to load my clan:', error);
        return of(null);
      })
    );
  }

  updateClan(
  clanId: number,
  payload: UpdateClanPayLoad
): Observable<ClanDto> {
  return this.httpClient.put<ClanDto>(
    `${this.baseUrl}/${clanId}`,
    payload
  );
}

  getClanById(clanId: number): Observable<ClanDto> {
    return this.httpClient.get<ClanDto>(`${this.baseUrl}/${clanId}`);
  }

  getTopClans(): Observable<ClanLeaderboardDto[]> {
    return this.httpClient.get<ClanLeaderboardDto[]>(`${this.baseUrl}/top`);
  }

  createClan(payload: CreateClanPayload): Observable<ClanDto> {
    return this.httpClient.post<ClanDto>(this.baseUrl, {
      name: payload.name,
      description: payload.description ?? '',
      invite: payload.invite ?? false,
    });
  }

  joinClan(clanId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.baseUrl}/${clanId}/join`, {});
  }

  leaveClan(clanId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/${clanId}/leave`);
  }

  getClanContributions(clanId: number): Observable<ClanMemberContributionDto[]> {
  return this.httpClient.get<ClanMemberContributionDto[]>(
    `${this.baseUrl}/${clanId}/contributions`
  );
}

getPendingRequests(clanId: number): Observable<ClanMemberDto[]> {
  return this.httpClient.get<ClanMemberDto[]>(
    `${this.baseUrl}/${clanId}/pending`
  );
}

acceptPendingMember(clanId: number, targetUserUuid: string): Observable<void> {
  return this.httpClient.post<void>(
    `${this.baseUrl}/${clanId}/pending/${targetUserUuid}/accept`,
    {}
  );
}

declinePendingMember(clanId: number, targetUserUuid: string): Observable<void> {
  return this.httpClient.post<void>(
    `${this.baseUrl}/${clanId}/pending/${targetUserUuid}/decline`,
    {}
  );
}

kickMember(clanId: number, targetUserUuid: string): Observable<void> {
  return this.httpClient.delete<void>(
    `${this.baseUrl}/${clanId}/kick?targetUserId=${targetUserUuid}`
  );
}

transferLeadership(clanId: number, newLeaderUserUuid: string): Observable<void> {
  return this.httpClient.post<void>(
    `${this.baseUrl}/${clanId}/leadership/transfer?newLeaderUserId=${newLeaderUserUuid}`,
    {}
  );
}
}
