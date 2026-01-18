package com.strongBeton.strongBeton.DTO;

import org.springframework.web.multipart.MultipartFile;

public class ImageModel {

    private String name;
    private MultipartFile file;

    public ImageModel(String name, MultipartFile file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
