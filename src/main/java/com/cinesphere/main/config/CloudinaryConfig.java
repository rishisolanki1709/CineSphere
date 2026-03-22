package com.cinesphere.main.config;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "doygt6b6y");
        config.put("api_key", "696622926839182");
        config.put("api_secret", "aAui1s_ri_xDFxZmEZV7Rl5C678");
        return new Cloudinary(config);
    }
}