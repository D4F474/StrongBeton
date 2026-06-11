import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { Topbar } from '../../navigation/topbar/topbar';
import { Sidebar } from '../../navigation/sidebar/sidebar';
import { BottomNav } from '../../navigation/bottom-nav/bottom-nav';

@Component({
  selector: 'app-app-layout',
  imports: [
    RouterOutlet,
    Topbar,
    Sidebar,
    BottomNav
  ],
  templateUrl: './app-layout.html',
  styleUrl: './app-layout.scss',
})
export class AppLayout {}