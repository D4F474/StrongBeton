import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ImageData } from '../common/image-data';
import { ImageSend } from '../common/image-send';
import { ImageResponse } from '../common/image-response';


@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private httpClient: HttpClient) { }

    private baseUrl = "http://localhost:8081/api/"

    getProfileImage() : Observable<ImageData>{
        const Url = `${this.baseUrl}getPhoto`; 
        return this.httpClient.get<ImageData>(Url);    
    }

    addProfileImage(file: File): Observable<ImageResponse> {
  const url = `${this.baseUrl}uploadPhoto`;

  const formData = new FormData();
  formData.append("file", file);
  formData.append("name", "avatar"); 
  return this.httpClient.post<ImageResponse>(url, formData);
}


    updateProfileImage(file: File): Observable<ImageResponse> {
  const url = `${this.baseUrl}updatePhoto`;

  const formData = new FormData();
  formData.append("file", file);
  formData.append("name", "avatar"); 
  console.log(formData);
  return this.httpClient.put<ImageResponse>(url, formData);
}


}
