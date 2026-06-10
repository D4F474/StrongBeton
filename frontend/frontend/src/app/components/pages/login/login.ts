import { RouterLink } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginDto } from '../../../common/user/login-dto';
import { AuthService } from '../../../services/auth-service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthState } from '../../../common/user/auth-state';

@Component({
  selector: 'app-login',
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login implements OnInit {

  logInForm!: FormGroup;
  submitError = '';
  isSubmitting = false;

constructor(
  private formBuilder: FormBuilder,
  private authService: AuthService,
  private authState: AuthState,
  private router: Router
) {}

ngOnInit(): void {
  this.logInForm = this.formBuilder.group({
    email: new FormControl('', [
      Validators.required,
      Validators.email
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(100)
    ])
  });
}

get email() {
  return this.logInForm.get('email');
}

get password() {
  return this.logInForm.get('password');
}

onSubmit(): void {
  this.submitError = '';

  if (this.logInForm.invalid) {
    this.logInForm.markAllAsTouched();
    this.submitError = 'Check the highlighted fields and try again.';
    return;
  }

  this.isSubmitting = true;

  const user = new LoginDto();
  user.email = this.logInForm.value.email;
  user.password = this.logInForm.value.password;

  this.authService.login(user).subscribe({
    next: () => {
      this.isSubmitting = false;
      this.router.navigate(['/app/home']);
    },
    error: (err) => {
      this.isSubmitting = false;
      this.authState.clear();
      this.submitError = this.resolveSubmitError(err);
    }
  });
}

hasError(controlName: string, errorName?: string): boolean {
  const control = this.logInForm.get(controlName);

  if (!control || !(control.touched || control.dirty)) {
    return false;
  }

  return errorName ? control.hasError(errorName) : control.invalid;
}

private resolveSubmitError(err: unknown): string {
  if (typeof err === 'object' && err !== null && 'error' in err) {
    const error = (err as { error?: unknown }).error;

    if (typeof error === 'string') {
      return error;
    }

    if (typeof error === 'object' && error !== null) {
      const problem = error as { detail?: string; description?: string };
      return problem.detail || problem.description || 'Sign in failed.';
    }
  }

  return 'Sign in failed.';
}
}
