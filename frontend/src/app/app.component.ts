import { Component } from '@angular/core';
import { AuthService } from './service/auth.service';
import { Router } from '@angular/router'
import { ImageService } from './service/image.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'strongBeton';

   constructor(private authService: AuthService,
               private router: Router,
              private imageService: ImageService){
              
              
              }


    ngOnInit(){
        
        this.authService.initializeSession();
    }

  public isLogged(): boolean{
    return this.authService.isAuthenticated();
  }
    public hideAside(): boolean{
    const hiddenRoutes = ['/settings', '/register-form', '/login-form','/user-profile-settings'];
    const currentUrl = this.router.url;
    const isUserProfile = currentUrl.startsWith('/user-info');
    return hiddenRoutes.includes(currentUrl) || isUserProfile;

    }

}
