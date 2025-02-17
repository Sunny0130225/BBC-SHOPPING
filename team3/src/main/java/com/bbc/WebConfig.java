package com.bbc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/upload";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + UPLOAD_DIR + "/");
        registry.addResourceHandler("/picture/**")
        .addResourceLocations("classpath:/static/picture/"); // 使用 classpath
    }
  
}


