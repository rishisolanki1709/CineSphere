package com.cinesphere.main.config;
import com.cloudinary.Cloudinary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

	@Value("${CLOUDINARY_CLOUD_NAME}")
	private String cloudName;
	
	@Value("${CLOUDINARY_API_KEY}")
	private String API_KEY;
	
	@Value("${CLOUDINARY_API_SECRET}")
	private String API_SEC;
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SEC);
        return new Cloudinary(config);
    }
}