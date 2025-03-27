package com.ecommerce.service.Impl;

import com.cloudinary.Cloudinary;
import com.ecommerce.service.CloudinaryImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
    private final Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file) {
        Map result = new HashMap();
        try{
            result = cloudinary.uploader().upload(file.getBytes(),Map.of());
        }
        catch(IOException e){
            throw new RuntimeException("Image Upload Error");
        }
        return result;
    }
}
