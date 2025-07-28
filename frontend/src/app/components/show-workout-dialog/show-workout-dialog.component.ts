import { Component, Inject, Input } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Workout } from '../../common/workout';
import { WorkoutService } from '../../service/workout.service';

@Component({
  selector: 'app-show-workout-dialog',
  standalone: false,
  
  templateUrl: './show-workout-dialog.component.html',
  styleUrl: './show-workout-dialog.component.css'
})
export class ShowWorkoutDialogComponent {

  constructor(public dialogRef: MatDialogRef<ShowWorkoutDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: Workout[],
       private workoutService: WorkoutService){
        
      }

      deleteWorkout(theId: number){
        this.workoutService.deleteWorkout(theId).subscribe();
        this.onclick();
      }

      onclick() : void{
        this.dialogRef.close();
      }
}