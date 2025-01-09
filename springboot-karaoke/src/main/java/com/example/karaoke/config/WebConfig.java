package com.example.karaoke.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
    	// 同源政策
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允許所有路徑
                        .allowedOrigins("http://localhost:5173") // 允許的前端域名
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 允許的 HTTP 方法
                        .allowedHeaders("*") // 允許的 Header
                        .allowCredentials(true); // 允許傳遞憑據（如 Cookie）
            }
        };
    }
}
