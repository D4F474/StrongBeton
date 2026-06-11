import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { UserStatusDto } from '../common/social/user-status-dto';
import { FriendViewDto } from '../common/social/friend-view-dto';

@Injectable({
  providedIn: 'root',
})
export class FriendsService {
  private readonly baseUrl = '/users';

  constructor(private httpClient: HttpClient) {}

  getSuggestedUsers(): Observable<UserStatusDto[]> {
    return this.httpClient.get<UserStatusDto[]>(
      `${this.baseUrl}/ListAllUsernames`
    );
  }

  getFriends(username: string): Observable<FriendViewDto[]> {
    return this.httpClient.get<FriendViewDto[]>(
      `${this.baseUrl}/seeAllFriends/${encodeURIComponent(username)}`
    );
  }

  sendFriendRequest(username: string): Observable<void> {
    return this.httpClient.post<void>(
      `${this.baseUrl}/inviteFriendRequest/${encodeURIComponent(username)}`,
      {}
    );
  }

  acceptFriendRequest(username: string): Observable<void> {
    return this.httpClient.post<void>(
      `${this.baseUrl}/acceptFriendRequest/${encodeURIComponent(username)}`,
      {}
    );
  }

  declineFriendRequest(username: string): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.baseUrl}/declineFriendRequest/${encodeURIComponent(username)}`
    );
  }

  removeFriend(username: string): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.baseUrl}/removeFriend/${encodeURIComponent(username)}`
    );
  }
}
