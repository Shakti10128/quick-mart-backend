package com.ecommerce.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class ProjectConfig {
    @Value("${API_KEY}")
    private String api_key;

    @Value("${API_SECRET}")
    private String api_secret;

    @Value("${CLOUD_NAME}")
    private String cloud_name;

    @Bean
    public Cloudinary getCloudinary() {
        System.out.println(api_key + " " + api_secret + " " + cloud_name);
        Map<String, Object> configs = Map.of(
                "cloud_name",cloud_name,
                "api_key",api_key,
                "api_secret",api_secret,
                "secure",true
        );

        return new Cloudinary(configs);
    }
}
