import { FeedPostCommentDto } from './feed-post-comment-dto';

export interface FeedPostDto {
    id?: number | null;

  content?: string | null;

  type?: string | null;
  postType?: string | null;

  username?: string | null;

  user?: {
    id?: number;
    uuid?: string;
    username?: string | null;
    email?: string | null;
  } | null;

  likesCount?: number | null;
  commentsCount?: number | null;
  likedByMe?: boolean | null;

  likes?: unknown[] | null;
  comments?: FeedPostCommentDto[] | null;

  userUuid?: string | null;
 
  createdAt?: string | null;
  updatedAt?: string | null;
}
