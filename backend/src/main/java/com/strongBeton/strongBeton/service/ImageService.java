package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.ImageDataDTO;
import com.strongBeton.strongBeton.DTO.ImageModel;
import com.strongBeton.strongBeton.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ImageService {
    public ResponseEntity<Map> uploadImage(ImageModel imageModel, User user);
    public Optional<ImageDataDTO> getProfileImage(int user);
}
