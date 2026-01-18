import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WorkoutListComponent } from './components/workout-list/workout-list.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
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
import { ShowWorkoutDialogComponent } from './components/show-workout-dialog/show-workout-dialog.component';
import { HelpSiteComponent } from './components/help-site/help-site.component';
import { AboutUsComponent } from './components/about-us/about-us.component';
import { MyFriendListComponent } from './components/my-friend-list/my-friend-list.component';
import { AuthInterceptor } from './auth-interceptor';
import { UserInfoComponent } from './components/user-info/user-info.component';
import { SettingsComponent } from './components/settings/settings.component';


const routes: Routes =[
  {path: 'home', component:HomeComponent},
  {path: 'workouts', component: WorkoutListComponent, canActivate:[AuthGuard]},
  {path: 'workouts/:id', component: WorkoutDetailsComponent, canActivate:[AuthGuard]},
  {path: 'app-my-friend-list', component: MyFriendListComponent, canActivate:[AuthGuard]},
  {path: 'app-help-site', component: HelpSiteComponent},
  {path: 'app-about-us', component: AboutUsComponent},
  {path: 'login-form', component: LogInComponent },
  {path: 'register-form', component: RegisterComponent},
  {path: 'user-info/:username', component: UserInfoComponent, canActivate:[AuthGuard]}, 
  {path: 'user-profile-settings', component: SettingsComponent, canActivate:[AuthGuard]},
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
    UserProfileComponent,
    ShowWorkoutDialogComponent,
    MyFriendListComponent,
    HelpSiteComponent,
    AboutUsComponent,
    UserInfoComponent,
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
    SettingsComponent,
  ],
  providers: [WorkoutService,{provide: HTTP_INTERCEPTORS, useClass : AuthInterceptor, multi:true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
