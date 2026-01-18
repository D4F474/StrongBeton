package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.ImageDataDTO;
import com.strongBeton.strongBeton.DTO.ImageModel;
import com.strongBeton.strongBeton.dao.CloudPhotoRepository;
import com.strongBeton.strongBeton.entity.CloudPhoto;
import com.strongBeton.strongBeton.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.strongBeton.strongBeton.enums.PhotoType.OTHER;
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
            if (imageModel.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (imageModel.getFile().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Optional<CloudPhoto> profileImage = cloudPhotoRepository.findByUserUuidAndPhotoType(user.getId(), PROFILE.getText());

            if (profileImage.isPresent()) {
                profileImage.get().setPhotoUrl(cloudinaryService.uploadFile(imageModel.getFile(), "folder_1"));
            }else{
                profileImage.get().setId(UUID.randomUUID());
                profileImage.get().setPhotoUrl(cloudinaryService.uploadFile(imageModel.getFile(), "folder_1"));
                profileImage.get().setUploadedAt(LocalDateTime.now());
                profileImage.get().setActive(true);
                profileImage.get().setPhoto(PROFILE);
                profileImage.get().setUser(user);
                profileImage.get().setDescription("Fix me in code service uploadImage!!!");
                if (profileImage.get().getPhotoUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }
                cloudPhotoRepository.save(profileImage.get());
            }

            return ResponseEntity.ok().body(Map.of("url", profileImage.get().getPhotoUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ImageDataDTO getProfileImage(UUID userId) {
        Optional<CloudPhoto> cloudPhoto = cloudPhotoRepository.findByUserUuidAndPhotoType(userId, PROFILE.getText());
        ImageDataDTO imageDataDTO = new ImageDataDTO();
        if(cloudPhoto.isPresent()){
            imageDataDTO.setPhotoUrl(cloudPhoto.get().getPhotoUrl());
            imageDataDTO.setPhoto(cloudPhoto.get().getPhoto().getText());
            imageDataDTO.setDescription(cloudPhoto.get().getDescription());
            System.out.println("FROM DTO: " + imageDataDTO);
        }
        return imageDataDTO;
    }
}