package com.strongBeton.strongBeton.entity;

import com.strongBeton.strongBeton.enums.PhotoType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "photos")
public class CloudPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="uuid_photo")
    UUID id;
    @Column(name="photo_url")
    String photoUrl;
    @Column(name="photo_type")
    @Enumerated(EnumType.STRING)
    PhotoType photo;
    @Column(name="is_active")
    boolean isActive;
    @Column(name="uploaded_at")
    LocalDateTime uploadedAt;
    @Column(name="description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                     CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public PhotoType getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoType photo) {
        this.photo = photo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CloudPhoto{" +
                "id=" + id +
                ", photoUrl='" + photoUrl + '\'' +
                ", photo=" + photo +
                ", isActive=" + isActive +
                ", uploadedAt=" + uploadedAt +
                ", description='" + description + '\'' +
                ", user=" + user +
                '}';
    }
}
