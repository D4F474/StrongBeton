import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Friend } from '../common/friend';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {

  
   constructor(private httpClient: HttpClient) { }
  
    private baseUrl = "http://localhost:8081/users";

    getAllFriends(username: string) : Observable<Friend[]>{
          const url = this.baseUrl + "/seeAllFriends/" + username;
            return this.httpClient.get<Friend[]>(url);
    }

    inviteFriend(theId : number){
        const url = this.baseUrl + "inviteFriendRequest/"+ theId;
        return this.httpClient.post<Friend>(url, null);
    }
    

    //TUKA NQ SSTANE MAI S ID VIJ GO S JSON
    acceptRequest(theId: number){
      const url = this.baseUrl + "acceptFriendRequest/" + theId;
      return this.httpClient.post<Friend>(url, null);
    }

    
    removeFriend(theId: number){
      const url = this.baseUrl + "removeFriend/" + theId;
      return this.httpClient.delete<Friend>(url); 
    }
}

