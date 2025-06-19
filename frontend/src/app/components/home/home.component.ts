import { Component, OnInit } from '@angular/core';
import { LeaderboardService } from '../../service/leaderboard.service';
import { Leaderboard } from '../../common/leaderboard';

@Component({
  selector: 'app-home',
  standalone: false,
  
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  leaderBoard: Leaderboard[] = [];
  
  constructor(private leaderboardService: LeaderboardService){

  }
  

  ngOnInit(): void {
     this.leaderboardService.getLeaderBoard().subscribe(
      (data: Leaderboard[]) =>{
              this.leaderBoard = data;
            }
    );
    
  }




}
