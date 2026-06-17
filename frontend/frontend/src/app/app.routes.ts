import { Routes } from '@angular/router';

import { PublicLayout } from './components/layouts/public-layout/public-layout';
import { AppLayout } from './components/layouts/app-layout/app-layout';
import { feedModeratorGuard } from './guards/feed-moderator-guard-guard';

import { LandingPage } from './components/pages/landing-page/landing-page';
import { Login } from './components/pages/login/login';
import { Register } from './components/pages/register/register';

import { HomeDashboard } from './components/pages/home-dashboard/home-dashboard';
import { ActiveWorkout } from './components/pages/active-workout/active-workout';
import { Progress } from './components/pages/progress/progress';
import { Clans } from './components/pages/clans/clans';
import { Coach } from './components/pages/coach/coach';
import { Profile } from './components/pages/profile/profile';
import { WorkoutSummary } from './components/pages/workout-summary/workout-summary';
import { Settings } from './components/pages/settings/settings';
import { authGuard } from './guards/auth-guard';
import { Friends } from './components/pages/friends/friends';
import { Feed } from './components/pages/feed/feed';
import { AdminPanel } from './components/pages/admin-panel/admin-panel';


export const routes: Routes = [
  {
    path: '',
    component: PublicLayout,
    children: [
      { path: '', component: LandingPage },
      { path: 'login', component: Login },
      { path: 'register', component: Register },
    ],
  },
  {
    path: 'app',
    component: AppLayout,
    canActivate: [authGuard],
    canActivateChild: [authGuard],
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeDashboard },
      { path: 'workout', component: ActiveWorkout },
      { path: 'workout/summary', component: WorkoutSummary },
      { path: 'progress', component: Progress },
      { path: 'clans', component: Clans },
      { path: 'coach', component: Coach },
      { path: 'profile', component: Profile },
      { path: 'settings', component: Settings },
      { path: 'friends', component: Friends, canActivate: [authGuard] },
      { path: 'feed', component: Feed, canActivate: [authGuard] },
      { path: 'pravomoshten', component: AdminPanel, canActivate: [authGuard, feedModeratorGuard]}
    ],
  },
];
