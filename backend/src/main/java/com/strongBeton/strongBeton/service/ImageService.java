package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dto.user.ImageDataDTO;
import com.strongBeton.strongBeton.dto.user.ImageModel;
import com.strongBeton.strongBeton.entity.user.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public interface ImageService {
    public ResponseEntity<Map> uploadImage(ImageModel imageModel, User user);
    public Optional<ImageDataDTO> getProfileImage(int user);
}
