package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.entity.CloudPhoto;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    public String uploadFile(MultipartFile file, String folderName);

}
