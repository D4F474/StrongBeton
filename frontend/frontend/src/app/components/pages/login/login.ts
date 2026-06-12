import { RouterLink, Router } from '@angular/router';
import { ChangeDetectorRef, Component, DestroyRef, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { timeout, take, finalize } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { LoginDto } from '../../../common/user/login-dto';
import { AuthService } from '../../../services/auth-service';
import { AuthState } from '../../../common/user/auth-state';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login implements OnInit {
  logInForm!: FormGroup;
  submitError = '';
  isSubmitting = false;

  private readonly destroyRef = inject(DestroyRef);

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private authState: AuthState,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.logInForm = this.formBuilder.group({
      email: new FormControl('', [
        Validators.required,
        Validators.email,
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(100),
      ]),
    });
  }

  onSubmit(): void {
    this.submitError = '';

    if (this.logInForm.invalid) {
      this.logInForm.markAllAsTouched();
      this.submitError = 'Check the highlighted fields and try again.';
      this.syncView();
      return;
    }

    this.isSubmitting = true;
    this.syncView();

    const user = new LoginDto();
    user.email = this.logInForm.value.email;
    user.password = this.logInForm.value.password;


    this.authService.login(user)
      .pipe(
        timeout(10000),
        take(1),
        takeUntilDestroyed(this.destroyRef),
        finalize(() => {
          this.isSubmitting = false;
          this.syncView();
        })
      )
      .subscribe({
        next: (res) => {
          this.router.navigate(['/app/home']);
        },
        error: (err) => {

          this.authState.clear();
          this.submitError = this.resolveSubmitError(err);

          this.syncView();
        },
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
    if (typeof err === 'object' && err !== null) {
      const httpError = err as {
        status?: number;
        error?: unknown;
        message?: string;
        name?: string;
      };

      if (httpError.name === 'TimeoutError') {
        return 'Server is not responding. Try again later.';
      }

      if (typeof httpError.error === 'string') {
        return httpError.error;
      }

      if (httpError.status === 0) {
        return 'Cannot connect to the server.';
      }

      if (httpError.status === 401 || httpError.status === 403) {
        return 'Invalid email or password.';
      }

      if (httpError.status && httpError.status >= 500) {
        return 'Server error. Try again later.';
      }

      if (typeof httpError.error === 'object' && httpError.error !== null) {
        const problem = httpError.error as {
          detail?: string;
          description?: string;
          message?: string;
          error?: string;
        };

        return (
          problem.detail ||
          problem.description ||
          problem.message ||
          problem.error ||
          'Sign in failed.'
        );
      }
    }

    return 'Sign in failed.';
  }

  private syncView(): void {
    this.cdr.detectChanges();
  }
}