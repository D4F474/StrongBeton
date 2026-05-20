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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/getPhoto")
    public ResponseEntity<?> getPhoto(@AuthenticationPrincipal User currentUser) {
        try {
            System.out.println(imageService.getProfileImage(currentUser.getId()));
            return ResponseEntity.ok(imageService.getProfileImage(currentUser.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cant find this photo!");
        }
    }

    @PutMapping("/updatePhoto")
    public ResponseEntity<?> updatePhoto(ImageModel imageModel,@AuthenticationPrincipal User currentUser){
        try {
            return imageService.uploadImage(imageModel, currentUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cant upload this photo!");
        }
    }

    @PostMapping("/uploadPhoto")
    public ResponseEntity<?> upload(ImageModel imageModel, @AuthenticationPrincipal User currentUser) {
        try {
            return imageService.uploadImage(imageModel, currentUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cant update this photo!");
        }
    }
}
