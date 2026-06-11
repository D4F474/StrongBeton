export interface ClanLeaderboardDto {
    clanId: number | null;
    clanName: string | null;
    logoUrl: string | null;

    totalMembers: number | null;
    activeMembers: number | null;

    totalPoints: number | null;
    teamScore: number | null;
}
