package com.skmns.codingtest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptorConfig authInterceptorConfig;

    public WebConfig(AuthInterceptorConfig authInterceptorConfig) {
        this.authInterceptorConfig = authInterceptorConfig;
    }

    /**
     * CORS 설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081", "http://127.0.0.1:8081") // vue 로컬 서버
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * 정적 리소스 핸들링
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/uploads/")
                .setCachePeriod(3600);
    }

    /**
     * 인터셉터 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptorConfig)
                .addPathPatterns("/articles/**", "/files/**") // 인증 필요
                .excludePathPatterns("/auth/login", "/auth/register"); // 인증 제외
    }
}
