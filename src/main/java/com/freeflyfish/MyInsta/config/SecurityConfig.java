package com.freeflyfish.MyInsta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Включает поддержку веб-безопасности Spring Security
public class SecurityConfig {

    /**
     * Создаем бин PasswordEncoder для шифрования паролей.
     * BCrypt - это адаптивный алгоритм хеширования, который автоматически
     * увеличивает сложность хеширования со временем для защиты от brute-force атак.
     *
     * @return BCryptPasswordEncoder - кодировщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // BCrypt автоматически добавляет "соль" (salt) к каждому паролю,
        // что делает одинаковые пароли разными после хеширования
    }

    /**
     * Конфигурируем цепочку фильтров безопасности для HTTP запросов.
     * Фильтры Spring Security обрабатывают аутентификацию и авторизацию.
     *
     * @param http объект для настройки веб-безопасности
     * @return сконфигурированная цепочка фильтров безопасности
     * @throws Exception если конфигурация не удалась
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF (Cross-Site Request Forgery) защиту,
                // так как мы создаем REST API и не используем сессии браузера
                .csrf(csrf -> csrf.disable())

                // Настраиваем правила авторизации для разных URL-путей
                .authorizeHttpRequests(authz -> authz
                        // Разрешаем всем доступ к эндпоинтам регистрации и аутентификации
                        .requestMatchers("/api/auth/**").permitAll()
                        // Разрешаем всем просмотр постов (без аутентификации)
                        .requestMatchers("/api/posts/**").permitAll()
                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )

                // Настраиваем управление сессиями
                .sessionManagement(session -> session
                        // STATELESS означает, что Spring Security не будет создавать сессии
                        // и не будет ожидать сессии от клиента (типично для REST API)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}