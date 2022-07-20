package co.com.sofka.fullstackgame.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfiguration {
    @Value("${server.origin}")
    private String origin;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (!origin.isBlank()) {
                    LoggerFactory.getLogger("config").info("Allowed Origin ==> {}", origin);
                    registry.addMapping("/**").allowedOrigins(origin);
                }
            }
        };
    }
}
