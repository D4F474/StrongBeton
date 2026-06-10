import { ClanMemberDto } from "./clan-member-dto";

export interface ClanDto {
    id: number;
    name: string | null;
    description: string | null;

    logoUrl: string | null;

    totalXP: number | null;

    currLeague: string | null;

    invite?: boolean | null;
    isInvite?: boolean | null;

    clanPoints: number | null;

    createdAt: string  | null;

    members: ClanMemberDto[] | null;
}
