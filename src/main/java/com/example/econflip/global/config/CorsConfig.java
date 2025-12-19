package com.example.econflip.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "https://localhost:5173",
                "https://localhost:5174",
                "https://econflip.vercel.app"
        ));

        config.setAllowedMethods(List.of(
                "GET","POST","PUT","DELETE","PATCH","OPTIONS"
        ));

        // 허용 헤더
        config.setAllowedHeaders(List.of("*"));

        // 쿠키/JWT 인증 사용 시 필수
        config.setAllowCredentials(true);

        config.setExposedHeaders(List.of(
                "Set-Cookie",
                "Authorization"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",config);
        return source;
    }
}
