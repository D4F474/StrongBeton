import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { userDto } from '../../../common/user/user-dto';
import { AuthService } from '../../../services/auth-service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ImageService } from '../../../services/image-service';
import { ImageData } from '../../../common/image/image-data';
import { ProfileImageData } from '../../../common/image/profile-image-data';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {
  userDto?: userDto;
  updateForm?: FormGroup;

  photoPreviewUrl: string | null = null;
  selectedFile: File | null = null;

  constructor(
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private destroyRef: DestroyRef,
    private formBuilder: FormBuilder,
    private imageService: ImageService
  ) {}

  ngOnInit(): void {
  this.authService
    .getMe()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((user) => {
      this.userDto = user;

      this.updateForm = this.formBuilder.group({
        username: [user.username],
        email: [user.email],
        cm: [user.cm],
        kg: [user.kg],
        bornDate: [user.bornDate],
        gender: [user.gender],
        firstName: [user.firstName],
        lastName: [user.lastName],
        profilePhotoUrl: [user.profilePhotoUrl],
      });

      this.syncView();
    });

  this.loadProfileImage();
}

  public updateProfile(): void {
    if (!this.updateForm || !this.userDto) {
      return;
    }

    const formValue = this.updateForm.value;

    const updatedUser: Partial<userDto> = {
      id: this.userDto.id,
      username: formValue.username,
      email: formValue.email,
      cm: formValue.cm,
      kg: formValue.kg,
      bornDate: formValue.bornDate,
      gender: formValue.gender,
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      profilePhotoUrl: formValue.profilePhotoUrl,
    };

    this.authService
      .updateProfile(updatedUser)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((updated) => {
        this.userDto = {
          ...this.userDto!,
          ...updatedUser,
        };

        this.syncView();
      });
  }


image: ProfileImageData | null = null;
isUploadingPhoto = false;

public onFileSelected(event: Event): void {
  const input = event.target as HTMLInputElement;

  if (!input.files || input.files.length === 0) {
    return;
  }

  const file = input.files[0];

  if (!file.type.startsWith('image/')) {
    console.error('Selected file is not an image');
    return;
  }

  this.selectedFile = file;

  const reader = new FileReader();

  reader.onload = () => {
    this.photoPreviewUrl = reader.result as string;
    this.syncView();
  };

  reader.readAsDataURL(file);
}

public addPhotoProfile(): void {
  if (!this.selectedFile) {
    console.error('No image selected');
    return;
  }

  const file = this.selectedFile;
  this.isUploadingPhoto = true;

  this.imageService
    .addProfileImage(file)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (response) => {
        this.photoPreviewUrl = response.url;

        this.selectedFile = null;
        this.isUploadingPhoto = false;

        this.loadProfileImage();
        this.syncView();
      },
      error: (error) => {
        console.error('Photo upload failed:', error);

        this.isUploadingPhoto = false;
        this.syncView();
      },
    });
}

private loadProfileImage(): void {
  this.imageService
    .getProfileImage()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (image: ProfileImageData) => {
        this.image = image;
        this.photoPreviewUrl = image.photoUrl;
        this.syncView();
      },
      error: () => {
        this.image = null;
        this.photoPreviewUrl = null;
        this.syncView();
      },
    });
}

  private syncView(): void {
    this.cdr.markForCheck();
  }
}