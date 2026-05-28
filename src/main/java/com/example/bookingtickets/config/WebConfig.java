package com.example.bookingtickets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // Разрешаем CORS для всех эндпоинтов
        .allowedOrigins("http://localhost:5432", "http://localhost:5173", "http://localhost:3000",
            "https://booking-frontend-production-a8ab.up.railway.app", "http://localhost:8080") // Порты твоего фронтенда
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}