import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SaveWorkout } from '../../common/save-workout';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { BetonValidators } from '../../validators/beton-validators';


@Component({
  selector: 'app-add-workout-dialog',
  standalone: false,
  
  templateUrl: './add-workout-dialog.component.html',
  styleUrl: './add-workout-dialog.component.css'
})
export class AddWorkoutDialogComponent {

  addWorkoutForm ?: FormGroup;
  
  constructor(public dialogRef: MatDialogRef<AddWorkoutDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, public formBuilder: FormBuilder
   ){}

  ngOnInit(): void {
    this.addWorkoutForm = this.formBuilder.group({
      workout : this.formBuilder.group({
        workoutName : new FormControl('',[Validators.required,
          Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
         traningDate: new FormControl('',[Validators.required])
        })
      });
  }

get workoutName(){return this.addWorkoutForm?.get('workout.workoutName');}
get traningDate(){return this.addWorkoutForm?.get('workout.traningDate');}

  onSubmit() {
      let workout :SaveWorkout =  new SaveWorkout(0, new Date(this.traningDate?.value), this.workoutName?.value);
      this.dialogRef.close(workout);
    
    
  }
}
