export interface ClanMemberDto {
    id: number;
    username?: string | null;
    user?: {
    id?: number;
    uuid?: string;
    username?: string | null;
    email?: string | null;
  } | null;
    clanId: number;
    clanRoleType: string;
    points: number;
    joinedAt: string ;

}
