import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService } from '../../../services/auth-service';

import { FeedService } from '../../../services/feed-service';
import { FeedPostDto } from '../../../common/social/feed-post-dto';
import { FeedPostCommentDto } from '../../../common/social/feed-post-comment-dto';
@Component({
  selector: 'app-feed',
  imports: [CommonModule, FormsModule],
  templateUrl: './feed.html',
  styleUrl: './feed.scss',
})
export class Feed {
posts: FeedPostDto[] = [];

  pageReady = false;
  actionLoading = false;
  currentUserUuid: string | null = null;

  actionError: string | null = null;
  actionSuccess: string | null = null;

  newPostContent = '';
  commentDrafts: Record<number, string> = {};

  constructor(
    private feedService: FeedService,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadCurrentUser();
    this.loadFeed();
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
        this.loadFeed();
      },
    });
}

canDeletePost(post: FeedPostDto): boolean {
  return !!post.userUuid && !!this.currentUserUuid && post.userUuid === this.currentUserUuid;
}

  loadFeed(): void {
    this.pageReady = false;
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

  createPost(): void {
    const content = this.newPostContent.trim();

    if (!content || this.actionLoading) {
      return;
    }

    this.actionLoading = true;
    this.actionError = null;
    this.actionSuccess = null;
    this.syncView();

    this.feedService
      .createPost({
        content,
        type: 'TEXT',
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.newPostContent = '';
          this.actionLoading = false;
          this.actionSuccess = 'Post created.';

          this.loadFeed();
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to create post:', error);

          this.actionLoading = false;
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
    if (!post.id || this.actionLoading) {
      return;
    }

    this.actionLoading = true;
    this.actionError = null;
    this.actionSuccess = null;
    this.syncView();

    this.feedService
      .likePost(post.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.actionLoading = false;
          this.loadFeed();
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to like post:', error);

          this.actionLoading = false;
          this.actionError = 'Could not like post.';
          this.syncView();
        },
      });
  }

  addComment(post: FeedPostDto): void {
    if (!post.id || this.actionLoading) {
      return;
    }

    const content = (this.commentDrafts[post.id] ?? '').trim();

    if (!content) {
      return;
    }

    this.actionLoading = true;
    this.actionError = null;
    this.actionSuccess = null;
    this.syncView();

    this.feedService
      .commentPost(post.id, content)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.commentDrafts[post.id!] = '';
          this.actionLoading = false;

          this.loadFeed();
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to comment post:', error);

          this.actionLoading = false;
          this.actionError = 'Could not add comment.';
          this.syncView();
        },
      });
  }

  deletePost(post: FeedPostDto): void {
    if (!post.id || this.actionLoading) {
      return;
    }

    const confirmed = window.confirm('Delete this post?');

    if (!confirmed) {
      return;
    }

    this.actionLoading = true;
    this.actionError = null;
    this.actionSuccess = null;
    this.syncView();

    this.feedService
      .deletePost(post.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.actionLoading = false;
          this.actionSuccess = 'Post deleted.';

          this.loadFeed();
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to delete post:', error);

          this.actionLoading = false;
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

  getAuthorName(post: FeedPostDto): string {
    return post.username || post.user?.username || post.user?.email || 'Athlete';
  }

  getPostType(post: FeedPostDto): string {
    return String(post.type || post.postType || 'TEXT');
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

  getComments(post: FeedPostDto): FeedPostCommentDto[] {
    return post.comments ?? [];
  }

  formatDate(value?: string | null): string {
    if (!value) {
      return '';
    }

    return new Date(value).toLocaleString();
  }

  trackByPostId(index: number, post: FeedPostDto): number {
    return post.id ?? index;
  }

  trackByIndex(index: number): number {
    return index;
  }

  private syncView(): void {
    this.cdr.markForCheck();
  }
}
