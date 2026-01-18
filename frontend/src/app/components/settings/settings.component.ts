import { Component } from '@angular/core';
import { UserDetails } from '../../common/user-details';
import {UserService} from '../../service/user.service';
import { ImageData } from '../../common/image-data';
import { ImageService } from '../../service/image.service';
import { AuthService } from '../../service/auth.service';
import { AuthState } from '../../common/auth-state';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {

public userData: any;
public image!: ImageData;
selectedFile!: File;
constructor(private userService: UserService,
            private imageService: ImageService,
            private authService: AuthService,
            private authState: AuthState
            ){}


ngOnInit():void {
     this.userData = this.authState; 
    this.imageService.getProfileImage().subscribe(
        data=>{
            this.image = data;
        }
    )
}

onFileSelected(event:any){
    this.selectedFile = event.target.files[0];

}

addPhotoProfile(){
        if(this.image == null){
    this.imageService.addProfileImage(this.selectedFile).subscribe(
        data=>{
            console.log(data);
        }
    );
        }else{
        this.imageService.updateProfileImage(this.selectedFile).subscribe(
            data =>{
                console.log("Updated");
            }
        )
        
        }

}

}
