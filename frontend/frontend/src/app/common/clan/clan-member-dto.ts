export interface ClanMemberDto {
    id: number;

  userUuid?: string | null;
  username?: string | null;
  uuid?: string | null;

  user?: {
    uuid?: string | null;
    username?: string | null;
    email?: string | null;
  } | null;

  clanId: number;
  clanRoleType: string;
  points: number;
  joinedAt: string;

}
