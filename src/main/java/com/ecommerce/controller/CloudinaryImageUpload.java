package com.ecommerce.controller;

import com.ecommerce.service.CloudinaryImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary/upload")
@RequiredArgsConstructor
public class CloudinaryImageUpload {

    private final CloudinaryImageService cloudinaryImageService;

    @PostMapping
    public ResponseEntity<Map> uploadImage(@RequestParam("profile") MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        Map data = cloudinaryImageService.upload(file);
        return ResponseEntity.ok(data);
    }

}
