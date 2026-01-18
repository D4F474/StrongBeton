import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { UserDetails } from '../../common/user-details';
import { AuthService } from '../../service/auth.service';
import { AuthState } from '../../common/auth-state';

@Component({
  selector: 'app-user-profile',
  standalone: false,
  
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent  implements OnInit {

    userData: any; 
    constructor(private authState: AuthState, private authService: AuthService){
        this.userData = this.authState.user;
    }

  ngOnInit(): void {
       }
  logout() : void{
    this.authService.logout();
  }
    
}
