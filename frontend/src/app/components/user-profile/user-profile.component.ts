import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { UserDetails } from '../../common/user-details';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-user-profile',
  standalone: false,
  
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent  implements OnInit {
    userData!:UserDetails;
    constructor(private userService: UserService, private authService: AuthService){

    }
  ngOnInit(): void {
     this.userService.getUser(this.authService.getToken()).subscribe(
      data =>{
     this.userData = data;   
      }
    );
  }

    
}
