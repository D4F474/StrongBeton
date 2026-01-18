import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { RegisterDTO } from '../../common/register-dto';
import { BetonValidators } from '../../validators/beton-validators';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,
  
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
    registerGroup?: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router
  )
  {
  
  }

  ngOnInit(): void {
    this.registerGroup = this.formBuilder.group({
      registerdto : this.formBuilder.group({
        username : new FormControl('',[Validators.required,
          Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
        password: new FormControl('', [Validators.required,
          Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
        confirmPassword: new FormControl('', [Validators.required,
          Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
        firstName: new FormControl('',[Validators.required,
          Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
        lastName: new FormControl('',[Validators.required,
          Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
        email: new FormControl('',[Validators.required,
            Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]),
        cityName: new FormControl('',[Validators.required,
          Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
        streetName: new FormControl('',[Validators.required,
          Validators.minLength(2), BetonValidators.notOnlyWhitespace]),
        cm: new FormControl('',[Validators.required]),
        kg: new FormControl('',[Validators.required]),
        bornDate: new FormControl('',[Validators.required]),
        gender: new FormControl('',[Validators.required])
      })
    });
  }

  get username(){return this.registerGroup?.get('registerdto.username');}
  get password(){return this.registerGroup?.get('registerdto.password');}
  get confirmPassword() {return this.registerGroup?.get('registerdto.confirmPassword');}
  get firstName(){return this.registerGroup?.get('registerdto.firstName');}
  get lastName(){return this.registerGroup?.get('registerdto.lastName');}
  get email(){return this.registerGroup?.get('registerdto.email');}
  get cityName(){return this.registerGroup?.get('registerdto.cityName');}
  get streetName(){return this.registerGroup?.get('registerdto.streetName');}
  get cm(){return this.registerGroup?.get('registerdto.cm');}
  get kg(){return this.registerGroup?.get('registerdto.kg');}
  get bornDate(){return this.registerGroup?.get('registerdto.bornDate');}
  get gender(){return this.registerGroup?.get('registerdto.gender');}

  onSubmit() {
    this.registerGroup?.markAllAsTouched();
    
    if (this.registerGroup?.valid) {
      console.log(`  
        ${this.username?.value}\n
        ${this.password?.value}\n
        ${this.confirmPassword?.value}\n
        ${this.firstName?.value}\n
        ${this.lastName?.value}\n
        ${this.email?.value}\n
        ${this.cityName?.value}\n
        ${this.streetName?.value}\n
        ${this.cm?.value}\n
        ${this.kg?.value}\n
        ${this.bornDate?.value}\n
        ${this.gender?.value}`);
        
        let register = new RegisterDTO();
        register.id = 0;
        register.username = this.username?.value;
        register.password = this.password?.value;
        register.firstName = this.firstName?.value;
        register.lastName = this.lastName?.value;
        register.email = this.email?.value;
        register.cityName = this.cityName?.value;
        register.streetName = this.streetName?.value;
        register.cm = this.cm?.value;
        register.kg = this.kg?.value;
        register.bornDate = this.bornDate?.value;
        register.gender = this.gender?.value;

        const registerDTO: RegisterDTO = JSON.parse(JSON.stringify(register));
        console.log(JSON.stringify(registerDTO));
        
        this.authService.register(registerDTO)
        .subscribe(
          data =>{
            this.router.navigate(['/login-form']);
          }
        );
    } else {
      console.log('Error!');
    }
  }


  
}
