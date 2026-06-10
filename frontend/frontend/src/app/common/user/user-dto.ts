import { FriendViewDTO } from "../friend-view-dto";

export interface userDto {
    id: string;
    username: string;
    firstName: string | null;
    lastName: string | null;
    cm: number | null;
    kg: number | null;
    bornDate: string | null;
    gender: string | null;
    friends: FriendViewDTO[];
    profilePhotoUrl: string | null;
    email: string;
}
