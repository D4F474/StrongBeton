import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FeedPostDto } from '../../../../../common/social/feed-post-dto';

// Смени тези типове с твоите реални DTO-та, ако вече ги имаш.
export type FeedPostDTO = any;

@Component({
  selector: 'app-feed-post-card',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feed-post-card.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FeedPostCardComponent {
  @Input({ required: true }) post!: FeedPostDTO;
  @Input() currentUserUuid: string | null = null;
  @Input() loading = false;

  
  @Output() like = new EventEmitter<FeedPostDTO>();
  @Output() delete = new EventEmitter<FeedPostDTO>();
  @Output() comment = new EventEmitter<{ post: FeedPostDTO; content: string }>();

  commentContent = '';

  submitComment(): void {
    const content = this.commentContent.trim();

    if (!content || this.loading) {
      return;
    }

    this.comment.emit({
      post: this.post,
      content,
    });

    this.commentContent = '';
  }

  getPostType(): string {
    return this.post?.type || this.post?.postType || 'TEXT POST';
  }

  getAuthorName(): string {
    return (
      this.post?.username ||
      this.post?.authorUsername ||
      this.post?.user?.username ||
      this.post?.user?.email ||
      'Athlete'
    );
  }

  canDeletePost(): boolean {
    return !!this.post?.userUuid &&
           !!this.currentUserUuid &&
           this.post.userUuid === this.currentUserUuid;
  }

  getLikesCount(): number {
    if (typeof this.post?.likesCount === 'number') {
      return this.post.likesCount;
    }

    if (Array.isArray(this.post?.likes)) {
      return this.post.likes.length;
    }

    return 0;
  }

  getComments(): any[] {
    return this.post?.comments || [];
  }

  getCommentsCount(): number {
    return this.getComments().length;
  }

  getCommentAuthor(comment: any): string {
    return (
      comment?.username ||
      comment?.user?.username ||
      comment?.user?.email ||
      'Athlete'
    );
  }

  formatDate(value: string | Date | null | undefined): string {
    if (!value) {
      return '';
    }

    return new Date(value).toLocaleString('bg-BG', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  trackByIndex(index: number): number {
    return index;
  }
  
  isLikedByCurrentUser(): boolean {
  if (!this.currentUserUuid) {
    return false;
  }

  return (this.post.likes ?? []).some((like: any) => {
    return (
      like.userUuid === this.currentUserUuid ||
      like.user?.id === this.currentUserUuid ||
      like.user?.uuid === this.currentUserUuid
    );
  });
}
}