package com.freeflyfish.MyInsta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // Разрешаем все origins
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Разрешаем все методы
        configuration.setAllowedHeaders(Arrays.asList("*")); // Разрешаем все заголовки
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Включаем CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Отключаем CSRF для REST API
                .csrf(csrf -> csrf.disable())

                // Разрешаем frames для H2 console
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )

                // Настраиваем правила авторизации
                .authorizeHttpRequests(authz -> authz
                        // Разрешаем доступ к H2 console
                        .requestMatchers("/h2-console/**").permitAll()

                        // Разрешаем ВСЕ API запросы
                        .requestMatchers("/api/**").permitAll()

                        // Все остальные запросы запрещаем (чтобы Spring не искал статические ресурсы)
                        .anyRequest().denyAll()
                );

        return http.build();
    }
}