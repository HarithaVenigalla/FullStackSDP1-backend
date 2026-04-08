package com.agrivalue.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@org.springframework.lang.NonNull ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get("uploads/products");
        String uploadAbsPath = uploadPath.toFile().getAbsolutePath();
        
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:/" + uploadAbsPath + "/");
    }
}
