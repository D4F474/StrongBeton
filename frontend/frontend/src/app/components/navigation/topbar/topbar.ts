import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-topbar',
  imports: [CommonModule, RouterLink],
  templateUrl: './topbar.html',
  styleUrl: './topbar.scss',
})
export class Topbar {
  logoAvailable = true;

  constructor(private authService: AuthService) {}

  hideLogo(): void {
    this.logoAvailable = false;
  }

  logout(): void {
  this.authService.logout();
}
}
