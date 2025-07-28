import { Component, OnInit } from '@angular/core';
import { Friend } from '../../common/friend';
import { FriendsService } from '../../service/friends.service';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';
import { UserDetails } from '../../common/user-details';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-my-friend-list',
  standalone: false,
  
  templateUrl: './my-friend-list.component.html',
  styleUrl: './my-friend-list.component.css'
})
export class MyFriendListComponent implements OnInit {
  public listOfFriends:Friend[] = [];
  private userDetails!: UserDetails;
  
  constructor(private friendsService: FriendsService,
    private userService: UserService,
    private authService: AuthService
  ){
    //POPRAVI ME UNDEFINED NA USSERNMAE
  }
  ngOnInit(): void {
  forkJoin({
    userDetails: this.userService.getUser(this.authService.getToken())
  }).subscribe(({ userDetails }) => {
    this.userDetails = userDetails;
    this.loadFriends();
  });
}

    public haveFriends():boolean{
    
      return this.listOfFriends.length > 0;
    }

    public loadFriends(){
        this.friendsService.getAllFriends(this.userDetails.username).subscribe(data =>{
          
          this.listOfFriends = data;
        });
    }

    public removeFriend(){
        
    }
  }