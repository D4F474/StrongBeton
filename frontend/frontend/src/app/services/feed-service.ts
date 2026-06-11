import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { FeedPostDto } from '../common/social/feed-post-dto';
import { FeedPostCommentDto } from '../common/social/feed-post-comment-dto';
import { CreatePostPayload } from '../common/social/create-post-payload';

@Injectable({
  providedIn: 'root',
})
export class FeedService {
   private readonly baseUrl = '/api';

  constructor(private httpClient: HttpClient) {}

  getFeed(): Observable<FeedPostDto[]> {
    return this.httpClient.get<FeedPostDto[]>(`${this.baseUrl}/loadPosts`);
  }

  createPost(payload: CreatePostPayload): Observable<FeedPostDto> {
    return this.httpClient.post<FeedPostDto>(
      `${this.baseUrl}/createPost`,
      payload
    );
  }

  likePost(postId: number): Observable<string> {
    return this.httpClient.post(
      `${this.baseUrl}/likePost/${postId}`,
      {},
      { responseType: 'text' }
    );
  }

  commentPost(postId: number, content: string): Observable<FeedPostCommentDto> {
    return this.httpClient.post<FeedPostCommentDto>(
      `${this.baseUrl}/commentPost/${postId}`,
      { content }
    );
  }

  deletePost(postId: number): Observable<string> {
    return this.httpClient.delete(
      `${this.baseUrl}/deletePost/${postId}`,
      { responseType: 'text' }
    );
  }
}
