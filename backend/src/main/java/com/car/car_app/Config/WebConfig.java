package com.car.car_app.config;

import com.car.car_app.security.RoleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // ✅ ADMIN فقط : gestion des utilisateurs
        registry.addInterceptor(new RoleInterceptor(Set.of("ADMIN")))
                .addPathPatterns("/api/utilisateurs/**")
                .excludePathPatterns("/api/utilisateurs/public/**");

        registry.addInterceptor(new RoleInterceptor(Set.of("VENDEUR")))
                .addPathPatterns("/api/vendeur/**");

        registry.addInterceptor(new RoleInterceptor(Set.of("ACHETEUR")))
                .addPathPatterns("/api/acheteur/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }
}
