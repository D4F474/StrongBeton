import { Component } from '@angular/core';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'strongBeton';

   constructor(private authService: AuthService){}

  public isLogged(): boolean{
    return this.authService.isAuthenticated();
  }
}
