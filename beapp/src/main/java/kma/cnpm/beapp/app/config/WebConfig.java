package kma.cnpm.beapp.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${urlClient}")
    private String urlClient;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(urlClient)  // Chỉ định URL cụ thể
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS")  // Chỉ định các phương thức cụ thể
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);  // Cache các cấu hình CORS này trong 1 giờ
    }
}
