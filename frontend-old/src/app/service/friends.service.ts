import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Friend } from '../common/friend';
import { Observable } from 'rxjs';
import { UserStatus } from '../common/user-status';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {
   
   constructor(private httpClient: HttpClient) { }
  
    private baseUrl = "http://localhost:8081/users";
    //private baseUrl = 'http://192.168.0.104:8081/users';

    getAllUsernames() : Observable<UserStatus[]>{
          const url = this.baseUrl + "/ListAllUsernames";
            return this.httpClient.get<UserStatus[]>(url);
    }

    getAllFriends(username : string) : Observable<Friend[]>{
          const url = this.baseUrl + "/seeAllFriends/"+ username;
            return this.httpClient.get<Friend[]>(url);
    }

    inviteFriend(username : String){
        const url = this.baseUrl + "/inviteFriendRequest/"+ username;
        return this.httpClient.post<Friend>(url, null);
    }
    

    acceptRequest(username : String){
      const url = this.baseUrl + "/acceptFriendRequest/" + username;
        console.log(username);
      return this.httpClient.post<Friend>(url, null);
    }

    
    removeFriend(username : String){
      const url = this.baseUrl + "/removeFriend/" + username;
      return this.httpClient.delete<Friend>(url); 
    }
   
    declineRequest(username: string) {
      const url = this.baseUrl + "/declineFriendRequest/" + username;
      return this.httpClient.delete<Friend>(url);
    }
}

