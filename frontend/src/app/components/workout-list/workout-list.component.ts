import { Component, OnInit } from '@angular/core';
import { WorkoutService } from '../../service/workout.service';
import { Workout } from '../../common/workout';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MatDialog } from '@angular/material/dialog';
import { AddWorkoutDialogComponent } from '../add-workout-dialog/add-workout-dialog.component';
import { UserService } from '../../service/user.service';
import { UserDetails } from '../../common/user-details';
import { AuthService } from '../../service/auth.service';
import { switchMap } from 'rxjs';
import { SaveWorkout } from '../../common/save-workout';


@Component({
  selector: 'app-workout-list',
  standalone: false,
  
  
  templateUrl: './workout-list.component.html',
  styleUrl: './workout-list.component.css'
})
export class WorkoutListComponent implements OnInit {
  
  workouts : Workout[] = [];
  currentWorkoutId: number = 1;
  newWorkoutName: String = '';
  user!: UserDetails;
  searchMode: boolean = false;
  wordToFind: string = '';

  constructor(private workoutService: WorkoutService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private userService: UserService,
    private authService: AuthService
  ){ }
  
  ngOnInit(): void {
  this.route.paramMap
    .pipe(
      switchMap(params => {
        const searchKeyword = params.get('keyword');
        if (searchKeyword) {
          this.searchMode = true;
          this.wordToFind = searchKeyword;
        }
        
        return this.userService.getUser(this.authService.getToken());
      })
    )
    .subscribe(userData => {
      this.user = userData;
      if (this.searchMode) {
        this.workoutService.getWorkouts(this.user.id).subscribe((data: Workout[]) => {
          this.workouts = data;
          this.findTraning();
        });
      } else {
        this.listWorkouts();
      }
    });
}
  
  findTraning(keyword: string = this.wordToFind) {
  this.searchMode = true;
  this.wordToFind = keyword;
  
  if (this.searchMode && this.wordToFind && this.wordToFind.trim() !== '') {
    const findedWorkouts = [];
    this.workoutService.getSearchBarData(this.wordToFind,this.user.id).subscribe(
      (data: Workout[]) =>{
        this.workouts = data;
      }
    );
    /*for (let i = 0; i < this.workouts.length; i++) {
      if (this.workouts[i].workoutName.toLowerCase().includes(this.wordToFind.toLowerCase())) {
        findedWorkouts.push(this.workouts[i]);
      }
    }
    this.workouts = findedWorkouts;
    */
  } else {
    this.listWorkouts();
  }
}

  listWorkouts() {  
    this.workoutService.getWorkouts(this.user.id)
    .subscribe((data: Workout[]) => {
      this.workouts = data;
    });
  }

  
  openAddWorkoutDialog(): void {
    const dialogRef = this.dialog.open(AddWorkoutDialogComponent, {
      height: '250px',
      width: '350px',
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.saveWorkout(result);
      }
    });
  }
 
  saveWorkout(workout: SaveWorkout){
    console.log("I want to save workout: " + workout);
    const JSONFile = JSON.parse(JSON.stringify(workout));
    console.log(JSONFile);
    this.ngOnInit();
  
  this.workoutService.saveWorkout(JSONFile, this.user.id).subscribe();
  }

  deleteWorkout(theId : number){
    this.workoutService.deleteWorkout(theId).subscribe();
    
  }
}


