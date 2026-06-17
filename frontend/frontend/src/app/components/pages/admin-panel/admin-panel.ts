import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  OnInit,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { AdminFeedComment } from '../../../common/admin-feed-comment';
import { AdminFeedPost } from '../../../common/admin-feed-post';
import { AdminFeedService, FeedFilter } from '../../../services/admin-feed-service';
import { AdminFeedStats } from '../../../common/admin-feed-stats';

@Component({
  selector: 'app-admin-panel',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-panel.html',
  styleUrl: './admin-panel.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdminPanel implements OnInit {
  stats: AdminFeedStats | null = null;

  posts: AdminFeedPost[] = [];
  selectedPost: AdminFeedPost | null = null;
  selectedPostComments: AdminFeedComment[] = [];

  search = '';
  activeFilter: FeedFilter = 'ALL';

  isLoading = false;
  isLoadingComments = false;
  errorMessage = '';

  private busyCommentIds = new Set<number>();

  filters: { label: string; value: FeedFilter }[] = [
    { label: 'All', value: 'ALL' },
    { label: 'Visible', value: 'VISIBLE' },
    { label: 'Hidden', value: 'HIDDEN' },
    { label: 'Pinned', value: 'PINNED' },
    { label: 'Reported', value: 'REPORTED' },
  ];

  constructor(
    private adminFeedService: AdminFeedService,
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadPanel();
  }

  loadPanel(): void {
    this.errorMessage = '';
    this.syncView();
    this.loadStats();
    this.loadPosts();
  }

  loadStats(): void {
    this.adminFeedService
      .getStats()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (stats) => {
          this.stats = stats;
          this.syncView();
        },
        error: () => {
          this.errorMessage = 'Could not load feed stats.';
          this.syncView();
        },
      });
  }

  loadPosts(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.syncView();

    this.adminFeedService
      .getPosts(this.search, this.activeFilter)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (posts) => {
          this.posts = posts ?? [];
          this.isLoading = false;
          this.syncView();
        },
        error: () => {
          this.posts = [];
          this.errorMessage = 'Could not load feed posts.';
          this.isLoading = false;
          this.syncView();
        },
      });
  }

  setFilter(filter: FeedFilter): void {
    this.activeFilter = filter;
    this.selectedPost = null;
    this.selectedPostComments = [];
    this.syncView();
    this.loadPosts();
  }

  selectPost(post: AdminFeedPost): void {
    this.selectedPost = post;
    this.selectedPostComments = [];
    this.syncView();
    this.loadComments(post.id);
  }

  loadComments(postId: number): void {
    this.isLoadingComments = true;
    this.errorMessage = '';
    this.syncView();

    this.adminFeedService
      .getPostComments(postId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (comments) => {
          this.selectedPostComments = comments ?? [];
          this.isLoadingComments = false;
          this.syncView();
        },
        error: () => {
          this.errorMessage = 'Could not load comments.';
          this.isLoadingComments = false;
          this.syncView();
        },
      });
  }

  toggleHidden(post: AdminFeedPost): void {
    const confirmed = confirm(
      post.hidden
        ? 'Make this post visible again?'
        : 'Hide this post from the feed?'
    );

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';
    this.syncView();

    this.adminFeedService
      .togglePostHidden(post.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.updatePost(post.id, { hidden: !post.hidden });
          this.loadStats();
          this.syncView();
        },
        error: () => {
          this.errorMessage = 'Could not change post visibility.';
          this.syncView();
        },
      });
  }

  togglePinned(post: AdminFeedPost): void {
    this.errorMessage = '';
    this.syncView();

    this.adminFeedService
      .togglePostPinned(post.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.updatePost(post.id, { pinned: !post.pinned });
          this.loadStats();
          this.syncView();
        },
        error: () => {
          this.errorMessage = 'Could not change pinned status.';
          this.syncView();
        },
      });
  }

  toggleCommentsLocked(post: AdminFeedPost): void {
    this.errorMessage = '';
    this.syncView();

    this.adminFeedService
      .toggleCommentsLocked(post.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.updatePost(post.id, {
            commentsLocked: !post.commentsLocked,
          });
          this.syncView();
        },
        error: () => {
          this.errorMessage = 'Could not change comments status.';
          this.syncView();
        },
      });
  }

  deletePost(post: AdminFeedPost): void {
    const confirmed = confirm(`Delete the post from ${post.username}?`);

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';
    this.syncView();

    this.adminFeedService
      .deletePost(post.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.posts = this.posts.filter((currentPost) => currentPost.id !== post.id);

          if (this.selectedPost?.id === post.id) {
            this.selectedPost = null;
            this.selectedPostComments = [];
          }

          this.loadStats();
          this.syncView();
        },
        error: () => {
          this.errorMessage = 'Could not delete post.';
          this.syncView();
        },
      });
  }

  toggleCommentHidden(comment: AdminFeedComment): void {
    if (this.isCommentBusy(comment.id)) {
      return;
    }

    this.errorMessage = '';
    this.setCommentBusy(comment.id, true);
    this.syncView();

    this.adminFeedService
      .toggleCommentHidden(comment.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.selectedPostComments = this.selectedPostComments.map((currentComment) =>
            currentComment.id === comment.id
              ? { ...currentComment, hidden: !currentComment.hidden }
              : currentComment
          );
          this.setCommentBusy(comment.id, false);
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to change comment visibility:', error);
          this.errorMessage = 'Could not change comment visibility.';
          this.setCommentBusy(comment.id, false);
          this.syncView();
        },
      });
  }

  deleteComment(comment: AdminFeedComment): void {
    if (this.isCommentBusy(comment.id)) {
      return;
    }

    const confirmed = confirm(`Delete the comment from ${comment.username}?`);

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';
    this.setCommentBusy(comment.id, true);
    this.syncView();

    this.adminFeedService
      .deleteComment(comment.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.selectedPostComments = this.selectedPostComments.filter(
            (currentComment) => currentComment.id !== comment.id
          );

          if (this.selectedPost) {
            this.updatePost(this.selectedPost.id, {
              commentsCount: Math.max(0, this.selectedPost.commentsCount - 1),
            });
          }

          this.loadStats();
          this.setCommentBusy(comment.id, false);
          this.syncView();
        },
        error: (error) => {
          console.error('Failed to delete comment:', error);
          this.errorMessage = 'Could not delete comment.';
          this.setCommentBusy(comment.id, false);
          this.syncView();
        },
      });
  }

  formatDate(value: string | null | undefined): string {
    if (!value) {
      return '-';
    }

    return new Date(value).toLocaleString('bg-BG', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  trackByPostId(_: number, post: AdminFeedPost): number {
    return post.id;
  }

  trackByCommentId(_: number, comment: AdminFeedComment): number {
    return comment.id;
  }

  isCommentBusy(commentId: number): boolean {
    return this.busyCommentIds.has(commentId);
  }

  private updatePost(postId: number, changes: Partial<AdminFeedPost>): void {
    this.posts = this.posts.map((post) =>
      post.id === postId ? { ...post, ...changes } : post
    );

    if (this.selectedPost?.id === postId) {
      this.selectedPost = {
        ...this.selectedPost,
        ...changes,
      };
    }
  }

  private syncView(): void {
    this.cdr.markForCheck();
  }

  private setCommentBusy(commentId: number, busy: boolean): void {
    const nextBusyCommentIds = new Set(this.busyCommentIds);

    if (busy) {
      nextBusyCommentIds.add(commentId);
    } else {
      nextBusyCommentIds.delete(commentId);
    }

    this.busyCommentIds = nextBusyCommentIds;
  }
}
