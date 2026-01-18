import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserDetails } from '../../common/user-details';
import {UserService} from '../../service/user.service';
import { ActivatedRoute } from '@angular/router';
import { FriendsService } from '../../service/friends.service';
import { Friend } from '../../common/friend';


@Component({
 standalone: false,
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.css'
})
export class UserInfoComponent {

   public  userData!: UserDetails ;
   public listOfFriends:Friend[] = []; 
  constructor(private user: UserService,
             private route: ActivatedRoute,
             private friends: FriendsService){}

ngOnInit(): void{
    let username : string = "";
    this.route.paramMap.subscribe(params => {
        username = params.get('username')!
    });


this.route.paramMap.subscribe(results =>{
    this.user.getUserInfo(username).subscribe(
        data => {
            this.userData = data;
        }
    );
})

this.route.paramMap.subscribe( result =>{
    this.friends.getAllFriends(username).subscribe(
        data => {
            this.listOfFriends = data;
            console.log(data);
        }
    );
});

}
}
