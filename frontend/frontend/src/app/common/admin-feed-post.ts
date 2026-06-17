export interface AdminFeedPost {
    id: number;
    uuid?: string | null;

    content: string;
    type?: string | null;
    postType?: string | null;

    username: string;
    userUuid?: string | null;
    profilePhotoUrl?: string | null;

    likesCount: number;
    commentsCount: number;
    reportsCount: number;

    hidden: boolean;
    pinned: boolean;
    commentsLocked: boolean;

    createdAt: string;
    updatedAt?: string | null;
}
