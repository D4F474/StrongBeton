import { ExerciseDto } from "./exercise-dto";

export interface WorkoutDetailsDto {
    id: number;
    exercise: ExerciseDto;
    volume?: number;
    estimatedOneRepMax?: number;
    exercisePoints?: number;
}
