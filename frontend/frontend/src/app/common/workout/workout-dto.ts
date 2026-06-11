export interface WorkoutDto {
    id: string;
    date: string;
    workoutName: string;
    total_tonnage_kg?: number;
    workoutScore?: number;
    workoutVolume?: number;
    status?: string;
}
