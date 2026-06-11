import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ImageData } from '../common/image/image-data';
import { ImageResponse } from '../common/image/image-response';
import { ProfileImageData } from '../common/image/profile-image-data';

@Injectable({
  providedIn: 'root',
})
export class ImageService {
  private readonly baseUrl = '/api/';

  constructor(private httpClient: HttpClient) {}

  getProfileImage(): Observable<ProfileImageData> {
    return this.httpClient.get<ProfileImageData>(`${this.baseUrl}getPhoto`);
  }

  addProfileImage(file: File): Observable<ImageResponse> {
    const formData = new FormData();

    formData.append('file', file);
    formData.append('name', 'avatar');

    return this.httpClient.post<ImageResponse>(
      `${this.baseUrl}uploadPhoto`,
      formData
    );
  }
}
