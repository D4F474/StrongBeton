import { RecentRecordDto } from "./recent-record-dto";
import { WeeklyVolumeDto } from "./weekly-volume-dto";

export interface StatsOverviewDto {
  strengthScore: number;
  strengthScoreDelta: number;

  weeklyVolume: number;
  weeklyVolumeTarget: number;
  weeklyVolumePercent: number;

  trainingStreak: number;
  bestStreak: number;

  personalRecords: number;
  recordsThisMonth: number;

  consistencyPercent: number;
  loadQuality: string;

  weeklyVolumeBars?: WeeklyVolumeDto[];
  recentRecords?: RecentRecordDto[];
}
