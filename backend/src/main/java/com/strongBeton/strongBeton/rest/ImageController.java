package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.user.ImageModel;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
