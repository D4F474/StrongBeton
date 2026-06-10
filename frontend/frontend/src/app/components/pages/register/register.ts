import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-register',
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register implements OnInit {
  currentStep = 1;
  registerGroup!: FormGroup;
  submitError = '';
  stepError = '';
  submitSuccess = '';
  isSubmitting = false;
  registrationComplete = false;

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService
  )
  {
    this.registerGroup = this.formBuilder.group({});
  }

  ngOnInit(): void {
    this.registerGroup = this.formBuilder.group({
      username: ['', [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(45),
        Validators.pattern(/^[a-zA-Z0-9_]+$/)
      ]],
      email: ['', [
        Validators.required,
        Validators.email,
        Validators.maxLength(100)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(100)
      ]],
      bornDate: ['', [
        Validators.required,
        this.pastDateValidator
      ]],
      cm: ['', [
        Validators.required,
        Validators.min(1),
        Validators.max(260)
      ]],
      kg: ['', [
        Validators.required,
        Validators.min(30.1),
        Validators.max(350)
      ]],
      gender: [true, Validators.required],
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
    this.submitSuccess = '';
    this.stepError = '';

    if (this.registerGroup.invalid) {
      this.registerGroup.markAllAsTouched();
      this.submitError = 'Check the highlighted fields and try again.';
      return;
    }

    this.isSubmitting = true;

    this.authService.register(this.registerGroup.value).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.registrationComplete = true;
        this.submitSuccess = 'Profile created. You can now sign in.';
      },
      error: (err) => {
        this.isSubmitting = false;
        this.submitError = this.resolveSubmitError(err);
      }
    });
  }

  hasError(controlName: string, errorName?: string): boolean {
    const control = this.registerGroup.get(controlName);

    if (!control || !(control.touched || control.dirty)) {
      return false;
    }

    return errorName ? control.hasError(errorName) : control.invalid;
  }

  private isCurrentStepValid(): boolean {
    return this.getCurrentStepControls().every((controlName) =>
      this.registerGroup.get(controlName)?.valid
    );
  }

  private markCurrentStepAsTouched(): void {
    this.getCurrentStepControls().forEach((controlName) => {
      this.registerGroup.get(controlName)?.markAsTouched();
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

  private pastDateValidator(control: AbstractControl) {
    if (!control.value) {
      return null;
    }

    const value = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return value < today ? null : { pastDate: true };
  }

  private resolveSubmitError(err: unknown): string {
    if (
      typeof err === 'object' &&
      err !== null &&
      'error' in err
    ) {
      const error = (err as { error?: unknown }).error;

      if (typeof error === 'string') {
        return error;
      }

      if (typeof error === 'object' && error !== null) {
        const problem = error as { detail?: string; description?: string };
        return problem.detail || problem.description || 'Registration failed.';
      }
    }

    return 'Registration failed.';
  }
}
