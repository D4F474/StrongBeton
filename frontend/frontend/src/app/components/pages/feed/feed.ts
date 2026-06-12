import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { AuthService } from '../../../services/auth-service';
import { FeedService } from '../../../services/feed-service';

import { FeedPostDto } from '../../../common/social/feed-post-dto';
import { FeedPostCommentDto } from '../../../common/social/feed-post-comment-dto';

import { FeedPostCardComponent } from './components/feed-post-card/feed-post-card';
import { CreatePostComponent } from './components/create-post/create-post';

@Component({
  selector: 'app-feed',
  imports: [
    CommonModule,
    CreatePostComponent,
    FeedPostCardComponent,
  ],
  templateUrl: './feed.html',
  styleUrl: './feed.scss',
})
export class Feed implements OnInit {
  posts: FeedPostDto[] = [];

  pageReady = false;
  createLoading = false;

  currentUserUuid: string | null = null;

  actionError: string | null = null;
  actionSuccess: string | null = null;

  private busyPostIds = new Set<number | string>();

  constructor(
    private feedService: FeedService,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadCurrentUser();
  }

  private loadCurrentUser(): void {
    this.authService
      .getMe()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (user) => {
          this.currentUserUuid = user.id ?? null;
          this.loadFeed();
        },
        error: (error) => {
          console.error('Failed to load current user:', error);
          this.currentUserUuid = null;
          this.loadFeed();
        },
      });
  }

  loadFeed(): void {
    this.pageReady = false;
    this.actionError = null;
    this.syncView();

    this.feedService
      .getFeed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (posts) => {
          this.posts = posts ?? [];
          this.pageReady = true;
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to load feed:', error);

          this.posts = [];
          this.pageReady = true;
          this.actionError = 'Could not load feed.';
          this.syncView();
        },
      });
  }

  createPost(content: string): void {
    const trimmedContent = content.trim();

    if (!trimmedContent || this.createLoading) {
      return;
    }

    this.createLoading = true;
    this.actionError = null;
    this.actionSuccess = null;
    this.syncView();

    this.feedService
      .createPost({
        content: trimmedContent,
        type: 'TEXT',
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (createdPost) => {
          this.createLoading = false;
          this.actionSuccess = 'Post created.';

          if (createdPost) {
            this.posts = [createdPost, ...this.posts];
          } else {
            this.loadFeed();
          }

          this.syncView();
        },
        error: (error) => {
          console.error('Failed to create post:', error);

          this.createLoading = false;
          this.actionError =
            error?.error?.message ||
            error?.error?.error ||
            error?.error ||
            error?.message ||
            'Could not create post.';

          this.syncView();
        },
      });
  }

  likePost(post: FeedPostDto): void {
  const postId = post.id;

  if (!postId || this.isPostBusy(postId)) {
    return;
  }

  this.actionError = null;
  this.actionSuccess = null;
  this.setPostBusy(postId, true);
  this.syncView();

  this.feedService
    .likePost(postId)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (result) => {
        const normalizedResult = result.toLowerCase();

        const wasLiked =
          normalizedResult.includes('liked') &&
          !normalizedResult.includes('unliked') &&
          !normalizedResult.includes('removed');

        const wasUnliked =
          normalizedResult.includes('unliked') ||
          normalizedResult.includes('removed') ||
          normalizedResult.includes('deleted');

        this.posts = this.posts.map((currentPost) => {
          if (currentPost.id !== postId) {
            return currentPost;
          }

          const currentLikesCount = this.getLikesCount(currentPost);

          return {
            ...currentPost,
            likesCount: wasUnliked
              ? Math.max(currentLikesCount - 1, 0)
              : wasLiked
                ? currentLikesCount + 1
                : currentLikesCount,
          };
        });

        this.setPostBusy(postId, false);
        this.syncView();
      },
      error: (error) => {
        console.error('Failed to toggle like:', error);

        this.setPostBusy(postId, false);
        this.actionError = 'Could not update like.';
        this.syncView();
      },
    });
}

  addComment(event: { post: FeedPostDto; content: string }): void {
    const postId = event.post.id;
    const content = event.content.trim();

    if (!postId || !content || this.isPostBusy(postId)) {
      return;
    }

    this.actionError = null;
    this.actionSuccess = null;
    this.setPostBusy(postId, true);
    this.syncView();

    this.feedService
      .commentPost(postId, content)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (createdComment) => {
          const commentToAdd: FeedPostCommentDto =
            createdComment ?? {
              content,
              username: 'You',
            };

          this.posts = this.posts.map((currentPost) => {
            if (currentPost.id !== postId) {
              return currentPost;
            }

            return {
              ...currentPost,
              comments: [
                ...(currentPost.comments ?? []),
                commentToAdd,
              ],
              commentsCount: this.getCommentsCount(currentPost) + 1,
            };
          });

          this.setPostBusy(postId, false);
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to comment post:', error);

          this.setPostBusy(postId, false);
          this.actionError = 'Could not add comment.';
          this.syncView();
        },
      });
  }

  deletePost(post: FeedPostDto): void {
    const postId = post.id;

    if (!postId || this.isPostBusy(postId)) {
      return;
    }

    const confirmed = window.confirm('Delete this post?');

    if (!confirmed) {
      return;
    }

    this.actionError = null;
    this.actionSuccess = null;
    this.setPostBusy(postId, true);
    this.syncView();

    this.feedService
      .deletePost(postId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.posts = this.posts.filter((currentPost) => currentPost.id !== postId);

          this.setPostBusy(postId, false);
          this.actionSuccess = 'Post deleted.';
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to delete post:', error);

          this.setPostBusy(postId, false);
          this.actionError =
            error?.error?.message ||
            error?.error?.error ||
            error?.error ||
            error?.message ||
            'Could not delete post.';

          this.syncView();
        },
      });
  }

  isPostBusy(postId: number | string | undefined | null): boolean {
    if (postId === undefined || postId === null) {
      return false;
    }

    return this.busyPostIds.has(postId);
  }

  private setPostBusy(postId: number | string, busy: boolean): void {
    const nextBusyPostIds = new Set(this.busyPostIds);

    if (busy) {
      nextBusyPostIds.add(postId);
    } else {
      nextBusyPostIds.delete(postId);
    }

    this.busyPostIds = nextBusyPostIds;
  }

  getLikesCount(post: FeedPostDto): number {
    if (typeof post.likesCount === 'number') {
      return post.likesCount;
    }

    return post.likes?.length ?? 0;
  }

  getCommentsCount(post: FeedPostDto): number {
    if (typeof post.commentsCount === 'number') {
      return post.commentsCount;
    }

    return post.comments?.length ?? 0;
  }

  trackByPostId(index: number, post: FeedPostDto): number {
    return post.id ?? index;
  }

  private syncView(): void {
    this.cdr.markForCheck();
  }

  private isPostLikedByCurrentUser(post: FeedPostDto): boolean {
  if (!this.currentUserUuid) {
    return false;
  }

  return (post.likes ?? []).some((like: any) => {
    if (typeof like === 'string') {
      return like === this.currentUserUuid;
    }

    return (
      like.userUuid === this.currentUserUuid ||
      like.user?.id === this.currentUserUuid ||
      like.user?.uuid === this.currentUserUuid
    );
  });
}

private removeCurrentUserLike(likes: any[]): any[] {
  if (!this.currentUserUuid) {
    return likes;
  }

  return likes.filter((like: any) => {
    if (typeof like === 'string') {
      return like !== this.currentUserUuid;
    }

    return (
      like.userUuid !== this.currentUserUuid &&
      like.user?.id !== this.currentUserUuid &&
      like.user?.uuid !== this.currentUserUuid
    );
  });
}

private addCurrentUserLike(likes: any[]): any[] {
  if (!this.currentUserUuid) {
    return likes;
  }

  const alreadyExists = likes.some((like: any) => {
    if (typeof like === 'string') {
      return like === this.currentUserUuid;
    }

    return (
      like.userUuid === this.currentUserUuid ||
      like.user?.id === this.currentUserUuid ||
      like.user?.uuid === this.currentUserUuid
    );
  });

  if (alreadyExists) {
    return likes;
  }

  return [
    ...likes,
    {
      userUuid: this.currentUserUuid,
    },
  ];
}
}