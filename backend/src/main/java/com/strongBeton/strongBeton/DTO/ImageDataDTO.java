package com.strongBeton.strongBeton.DTO;

import com.strongBeton.strongBeton.enums.PhotoType;

import java.time.LocalDateTime;

public class ImageDataDTO {

    String photoUrl;
    String photo;
    String description;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ImageDataDTO{" +
                "photoUrl='" + photoUrl + '\'' +
                ", photo=" + photo +
                ", description='" + description + '\'' +
                '}';
    }
}
