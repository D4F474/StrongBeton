export interface FeedPostCommentDto {
    id?: number | null;
  content?: string | null;
  username?: string | null;
  profilePhotoUrl: string;
  user?: {
    id?: number;
    uuid?: string;
    username?: string | null;
    email?: string | null;
  } | null;

  createdAt?: string | null;
  updatedAt?: string | null;
}
