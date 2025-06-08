package com.web.config;

import com.cloudinary.Cloudinary;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@SpringBootApplication
public class CloudConfig {
    @Bean
    public Cloudinary cloudinaryConfigs() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "djcdfgueh");
        config.put("api_key", "243597386739116");
        config.put("api_secret", "fEGZbSDxfcGO9WqPaI9T6sMAucU");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

}
