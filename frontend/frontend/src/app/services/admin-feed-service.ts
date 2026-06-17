import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AdminFeedComment } from '../common/admin-feed-comment';
import { AdminFeedPost } from '../common/admin-feed-post';
import { AdminFeedStats } from '../common/admin-feed-stats';

export type FeedFilter = 'ALL' | 'VISIBLE' | 'HIDDEN' | 'PINNED' | 'REPORTED';

@Injectable({
  providedIn: 'root',
})
export class AdminFeedService {
  private readonly baseUrl = '/api/admin/feed';

  constructor(private httpClient: HttpClient) {}

  getStats(): Observable<AdminFeedStats> {
    return this.httpClient.get<AdminFeedStats>(`${this.baseUrl}/stats`);
  }

  getPosts(search = '', filter: FeedFilter = 'ALL'): Observable<AdminFeedPost[]> {
    let params = new HttpParams().set('filter', filter);
    const trimmedSearch = search.trim();

    if (trimmedSearch) {
      params = params.set('search', trimmedSearch);
    }

    return this.httpClient.get<AdminFeedPost[]>(`${this.baseUrl}/posts`, {
      params,
    });
  }

  getPostComments(postId: number): Observable<AdminFeedComment[]> {
    return this.httpClient.get<AdminFeedComment[]>(
      `${this.baseUrl}/posts/${postId}/comments`
    );
  }

  togglePostHidden(postId: number): Observable<void> {
    return this.httpClient.patch<void>(
      `${this.baseUrl}/posts/${postId}/hidden`,
      {}
    );
  }

  togglePostPinned(postId: number): Observable<void> {
    return this.httpClient.patch<void>(
      `${this.baseUrl}/posts/${postId}/pinned`,
      {}
    );
  }

  toggleCommentsLocked(postId: number): Observable<void> {
    return this.httpClient.patch<void>(
      `${this.baseUrl}/posts/${postId}/comments-locked`,
      {}
    );
  }

  deletePost(postId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/posts/${postId}`);
  }

  toggleCommentHidden(commentId: number): Observable<void> {
    return this.httpClient.patch<void>(
      `${this.baseUrl}/comments/${commentId}/hidden`,
      {}
    );
  }

  deleteComment(commentId: number): Observable<void> {
    return this.httpClient.delete<void>(
      `${this.baseUrl}/comments/${commentId}`
    );
  }
}
