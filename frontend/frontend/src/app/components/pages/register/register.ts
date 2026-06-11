import { Component, DestroyRef, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register implements OnInit {
  currentStep = 1;
  registerGroup!: FormGroup;

  submitError = '';
  stepError = '';
  isSubmitting = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private destroyRef: DestroyRef
  ) {
    this.registerGroup = this.formBuilder.group({});
  }

  ngOnInit(): void {
    this.registerGroup = this.formBuilder.group(
      {
        username: [
          '',
          [
            Validators.required,
            Validators.minLength(2),
            Validators.maxLength(45),
            Validators.pattern(/^[a-zA-Z0-9_]+$/),
          ],
        ],
        email: [
          '',
          [
            Validators.required,
            Validators.email,
            Validators.maxLength(100),
          ],
        ],
        password: [
          '',
          [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(100),
          ],
        ],
        bornDate: [
          '',
          [
            Validators.required,
            this.pastDateValidator,
          ],
        ],
        cm: [
          '',
          [
            Validators.required,
            Validators.min(1),
            Validators.max(260),
          ],
        ],
        kg: [
          '',
          [
            Validators.required,
            Validators.min(30.1),
            Validators.max(350),
          ],
        ],
        gender: [true, Validators.required],
      },
      {
        updateOn: 'change',
      }
    );

    this.registerGroup.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.clearServerErrors();

        if (this.stepError && this.isCurrentStepValid()) {
          this.stepError = '';
        }

        if (this.submitError) {
          this.submitError = '';
        }
      });
  }

  nextStep(): void {
    this.stepError = '';
    this.submitError = '';

    if (!this.isCurrentStepValid()) {
      this.markCurrentStepAsTouched();
      this.stepError = 'Complete the highlighted fields before moving forward.';
      return;
    }

    if (this.currentStep < 3) {
      this.currentStep++;
    }
  }

  previousStep(): void {
    this.stepError = '';
    this.submitError = '';

    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  onSubmit(): void {
    this.submitError = '';
    this.stepError = '';

    if (this.registerGroup.invalid) {
      this.registerGroup.markAllAsTouched();
      this.currentStep = this.findFirstInvalidStep();
      this.submitError = 'Check the highlighted fields and try again.';
      return;
    }

    this.isSubmitting = true;

    this.authService.register(this.registerGroup.value).subscribe({
      next: () => {
        this.isSubmitting = false;

        this.router.navigate(['/login'], {
          queryParams: { registered: 'true' },
        });
      },
      error: (err) => {
        this.isSubmitting = false;
        this.applyBackendFieldErrors(err);
        this.currentStep = this.findFirstInvalidStep();
        this.submitError = this.resolveSubmitError(err);
      },
    });
  }

  hasError(controlName: string, errorName?: string): boolean {
    const control = this.registerGroup.get(controlName);

    if (!control || !(control.touched || control.dirty)) {
      return false;
    }

    return errorName ? control.hasError(errorName) : control.invalid;
  }

  getFieldStateClass(controlName: string): string {
    return this.hasError(controlName)
      ? 'border-red-300 bg-red-50 focus:border-red-500'
      : 'border-[#E5E7EB] bg-[#F7FAFC] focus:border-[#2563EB]';
  }

  getServerError(controlName: string): string {
    const control = this.registerGroup.get(controlName);
    const error = control?.getError('server');

    return typeof error === 'string' ? error : '';
  }

  private isCurrentStepValid(): boolean {
    return this.getCurrentStepControls().every((controlName) =>
      this.registerGroup.get(controlName)?.valid
    );
  }

  private markCurrentStepAsTouched(): void {
    this.getCurrentStepControls().forEach((controlName) => {
      this.registerGroup.get(controlName)?.markAsTouched();
      this.registerGroup.get(controlName)?.markAsDirty();
    });
  }

  private getCurrentStepControls(): string[] {
    if (this.currentStep === 1) {
      return ['username', 'email', 'password'];
    }

    if (this.currentStep === 2) {
      return ['bornDate', 'gender', 'kg', 'cm'];
    }

    return Object.keys(this.registerGroup.controls);
  }

  private findFirstInvalidStep(): number {
    const stepOneControls = ['username', 'email', 'password'];
    const stepTwoControls = ['bornDate', 'gender', 'kg', 'cm'];

    if (stepOneControls.some((controlName) => this.registerGroup.get(controlName)?.invalid)) {
      return 1;
    }

    if (stepTwoControls.some((controlName) => this.registerGroup.get(controlName)?.invalid)) {
      return 2;
    }

    return 3;
  }

  private pastDateValidator(control: AbstractControl) {
    if (!control.value) {
      return null;
    }

    const value = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return value < today ? null : { pastDate: true };
  }

  private applyBackendFieldErrors(err: unknown): void {
    const error = this.extractBackendError(err);
    const message = this.resolveSubmitError(err).toLowerCase();

    if (typeof error === 'object' && error !== null) {
      const possibleFieldErrors = error as {
        username?: string;
        email?: string;
        password?: string;
        bornDate?: string;
        kg?: string;
        cm?: string;
        errors?: Record<string, string>;
        fieldErrors?: Record<string, string>;
      };

      const fieldErrors =
        possibleFieldErrors.errors ||
        possibleFieldErrors.fieldErrors ||
        possibleFieldErrors;

      Object.entries(fieldErrors).forEach(([field, fieldMessage]) => {
        const control = this.registerGroup.get(field);

        if (control && typeof fieldMessage === 'string') {
          control.setErrors({
            ...control.errors,
            server: fieldMessage,
          });

          control.markAsTouched();
          control.markAsDirty();
        }
      });
    }

    if (message.includes('username')) {
      this.registerGroup.get('username')?.setErrors({
        ...this.registerGroup.get('username')?.errors,
        server: 'Username is already taken.',
      });
    }

    if (message.includes('email')) {
      this.registerGroup.get('email')?.setErrors({
        ...this.registerGroup.get('email')?.errors,
        server: 'Email is already used.',
      });
    }
  }

  private clearServerErrors(): void {
    Object.values(this.registerGroup.controls).forEach((control) => {
      if (!control.errors?.['server']) {
        return;
      }

      const { server, ...rest } = control.errors;

      control.setErrors(Object.keys(rest).length ? rest : null);
    });
  }

  private resolveSubmitError(err: unknown): string {
    const error = this.extractBackendError(err);

    if (typeof error === 'string') {
      return error;
    }

    if (typeof error === 'object' && error !== null) {
      const problem = error as {
        detail?: string;
        description?: string;
        message?: string;
      };

      return (
        problem.detail ||
        problem.description ||
        problem.message ||
        'Registration failed.'
      );
    }

    return 'Registration failed.';
  }

  private extractBackendError(err: unknown): unknown {
    if (typeof err === 'object' && err !== null && 'error' in err) {
      return (err as { error?: unknown }).error;
    }

    return null;
  }
}