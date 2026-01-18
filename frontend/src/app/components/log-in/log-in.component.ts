import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginDto } from '../../common/login-dto';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { BetonValidators } from '../../validators/beton-validators';

@Component({
  selector: 'app-log-in',
  standalone: false,
  
  templateUrl: './log-in.component.html',
  styleUrl: './log-in.component.css'
})
export class LogInComponent implements OnInit {

  logInForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router
  ){
    
  }

  ngOnInit(): void {
    this.logInForm = this.formBuilder.group({
      user: this.formBuilder.group({
        email: new FormControl('', [Validators.required,
            Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]),
        password: new FormControl('', [Validators.required,
                  Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
      })
    })
  }

  get email(){return this.logInForm?.get('user.email');}
  get password(){return this.logInForm?.get('user.password');}

  onSubmit(){
    let user = new LoginDto();
    user.email = this.email?.value;
    user.password = this.password?.value;

   let logInJSON = JSON.parse(JSON.stringify(user));
    this.authService.login(logInJSON).subscribe(
      data=>{
        this.router.navigate(['/home']);
        
      }
        
    );
  }

}
