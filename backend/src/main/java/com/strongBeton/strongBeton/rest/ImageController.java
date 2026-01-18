package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.ImageDataDTO;
import com.strongBeton.strongBeton.DTO.ImageModel;
import com.strongBeton.strongBeton.dao.CloudPhotoRepository;
import com.strongBeton.strongBeton.entity.CloudPhoto;
import com.strongBeton.strongBeton.entity.User;
import com.strongBeton.strongBeton.service.AuthService;
import com.strongBeton.strongBeton.service.ImageService;
import com.strongBeton.strongBeton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser;
    }

    @GetMapping("/getPhoto")
    public ResponseEntity<ImageDataDTO> getPhoto() {
        try {
            System.out.println(imageService.getProfileImage(getCurrentUser().getId()));
            return ResponseEntity.ok(imageService.getProfileImage(getCurrentUser().getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("/updatePhoto")
    public ResponseEntity<Map> updatePhoto(ImageModel imageModel){
        try {
            return imageService.uploadImage(imageModel, getCurrentUser());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping("/uploadPhoto")
    public ResponseEntity<Map> upload(ImageModel imageModel) {
        try {
            return imageService.uploadImage(imageModel, getCurrentUser());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
