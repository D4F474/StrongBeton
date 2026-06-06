import { Component, OnInit } from '@angular/core';
import { Friend } from '../../common/friend';
import { FriendsService } from '../../service/friends.service';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';
import { UserDetails } from '../../common/user-details';
import { forkJoin } from 'rxjs';
import { UserStatus } from '../../common/user-status';
import { MatDialog } from '@angular/material/dialog';
import { UserInfoComponent } from '../user-info/user-info.component'

@Component({
  selector: 'app-my-friend-list',
  standalone: false,

  templateUrl: './my-friend-list.component.html',
  styleUrl: './my-friend-list.component.css'
})
export class MyFriendListComponent implements OnInit {
  public listOfFriends:Friend[] = [];
  private userDetails!: UserDetails;
  public users: UserStatus[] = [];
   constructor(private friendsService: FriendsService,
    private userService: UserService,
    private dialog: MatDialog,
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
    this.loadUsers();
  });
}

    public loadUsers(){
      this.friendsService.getAllUsernames().subscribe(
        data => {
          this.users = data;
        }
      );
    }

    public haveFriends():boolean{

      return this.listOfFriends.length > 0;
    }

    public loadFriends(){
        this.friendsService.getAllFriends(this.userDetails.username).subscribe(data =>{
          this.listOfFriends = data;
        });
    }

    public removeFriend(username: string){
        this.friendsService.removeFriend(username).subscribe(
          data =>{
            this.loadFriends();
            this.loadUsers();
          }
        );
    }

    public addFreind(username: string){
        this.friendsService.inviteFriend(username).subscribe(data =>{
            this.loadFriends();
            this.loadUsers();
          });
    }

    public acceptFriend(username: string){
        this.friendsService.acceptRequest(username).subscribe(data =>{
            this.loadFriends();
            this.loadUsers();
          });
    }

    public declineFriend(username: string){
        this.friendsService.declineRequest(username).subscribe(data =>{
            this.loadFriends();
            this.loadUsers();
          });
    }

    public showDialogUserData(username:string){
    



    }

  }
