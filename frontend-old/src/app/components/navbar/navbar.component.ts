import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: false,
  
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

    menuOpen = false;

  constructor(private authService: AuthService){
        
  }


  isLogged():boolean{
    return this.authService.isAuthenticated();
  }



}
