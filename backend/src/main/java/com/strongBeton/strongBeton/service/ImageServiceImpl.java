package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dto.user.ImageDataDTO;
import com.strongBeton.strongBeton.dto.user.ImageModel;
import com.strongBeton.strongBeton.dao.CloudPhotoRepository;
import com.strongBeton.strongBeton.entity.CloudPhoto;
import com.strongBeton.strongBeton.entity.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.strongBeton.strongBeton.enums.PhotoType.PROFILE;

@Service
public class ImageServiceImpl implements ImageService {

    private CloudinaryService cloudinaryService;

    private CloudPhotoRepository cloudPhotoRepository;

    @Autowired
    public ImageServiceImpl(CloudinaryService cloudinaryService, CloudPhotoRepository cloudPhotoRepository) {
        this.cloudinaryService = cloudinaryService;
        this.cloudPhotoRepository = cloudPhotoRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<Map> uploadImage(ImageModel imageModel, User user) {
        try {
            if (imageModel == null || imageModel.getName() == null || imageModel.getName().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Името на снимката липсва!"));
            }
            if (imageModel.getFile() == null || imageModel.getFile().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Файлът липсва!"));
            }

            Optional<CloudPhoto> profileImageOpt = cloudPhotoRepository.findByUserIdAndPhotoType(user.getId(), PROFILE.getText());

            CloudPhoto cloudPhoto;

            if (profileImageOpt.isPresent()) {
                cloudPhoto = profileImageOpt.get();
            } else {
                cloudPhoto = new CloudPhoto();
                cloudPhoto.setId(UUID.randomUUID());
                cloudPhoto.setUploadedAt(LocalDateTime.now());
                cloudPhoto.setActive(true);
                cloudPhoto.setPhoto(PROFILE);
                cloudPhoto.setUser(user);
                cloudPhoto.setDescription("Profile image uploaded via API");
            }

            String uploadedUrl = cloudinaryService.uploadFile(imageModel.getFile(), "folder_1");

            if (uploadedUrl == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Грешка при качване в Cloudinary"));
            }

            cloudPhoto.setPhotoUrl(uploadedUrl);
            cloudPhotoRepository.save(cloudPhoto);

            return ResponseEntity.ok().body(Map.of("url", cloudPhoto.getPhotoUrl()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Възникна грешка: " + e.getMessage()));
        }
    }

    @Override
    public Optional<ImageDataDTO> getProfileImage(int userId) {
        return cloudPhotoRepository.findByUserIdAndPhotoType(userId, PROFILE.getText())
                .map(photo -> {
                    ImageDataDTO dto = new ImageDataDTO();
                    dto.setPhotoUrl(photo.getPhotoUrl());
                    dto.setPhoto(photo.getPhoto().getText());
                    dto.setDescription(photo.getDescription());
                    return dto;
                });
    }
}