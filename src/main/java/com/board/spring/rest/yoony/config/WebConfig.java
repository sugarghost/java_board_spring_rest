package com.board.spring.rest.yoony.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/v1/**")
        .allowedOrigins("http://localhost:8080")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .exposedHeaders("X-Total-Count",
            "Content-Range", "Content-Type", "Accept-Ranges", "Content-Length"
            , "Content-Disposition")
        .allowCredentials(true);
  }
}