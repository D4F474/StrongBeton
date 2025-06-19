import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WorkoutListComponent } from './components/workout-list/workout-list.component';
import { HttpClientModule } from '@angular/common/http';
import { WorkoutService } from './service/workout.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule, Routes } from '@angular/router';
import { WorkoutDetailsComponent } from './components/workout-details/workout-details.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { RegisterComponent } from './components/register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { HomeComponent } from './components/home/home.component';
import { AuthGuard } from './auth.guard';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SearchComponent } from './components/search/search.component';
import { AddWorkoutDialogComponent } from './components/add-workout-dialog/add-workout-dialog.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';


const routes: Routes =[
  {path: 'home', component:HomeComponent},
  {path: 'workouts', component: WorkoutListComponent, canActivate:[AuthGuard]},
  {path: 'workouts/:id', component: WorkoutDetailsComponent, canActivate:[AuthGuard]},
  {path: 'login-form', component: LogInComponent },
  {path: 'register-form', component: RegisterComponent},
  {path: 'search/:keyword', component: WorkoutListComponent},
  {path:'', redirectTo: 'home', pathMatch:'full'},
  {path: '**', redirectTo: 'home', pathMatch:'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    WorkoutListComponent,
    WorkoutDetailsComponent,
    LogInComponent,
    RegisterComponent,
    HomeComponent,
    NavbarComponent,
    SearchComponent,
    AddWorkoutDialogComponent,
    UserProfileComponent
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    NgbModule,
    MatDialogModule,
    MatButtonModule,
    
  ],
  providers: [WorkoutService],
  bootstrap: [AppComponent]
})
export class AppModule { }
