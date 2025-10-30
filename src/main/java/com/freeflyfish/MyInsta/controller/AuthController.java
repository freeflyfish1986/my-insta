package com.freeflyfish.MyInsta.controller;

import com.freeflyfish.MyInsta.dto.LoginRequest;
import com.freeflyfish.MyInsta.entity.User;
import com.freeflyfish.MyInsta.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController // Помечает класс как контроллер REST API, возвращающий данные (не представления)
@RequestMapping("/api/auth") // Базовый URL для всех эндпоинтов этого контроллера
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для внедрения зависимостей.
     * Spring автоматически передаст реализации UserService и PasswordEncoder
     * при создании экземпляра этого контроллера.
     */
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Эндпоинт для регистрации нового пользователя.
     * Доступен по URL: POST /api/auth/register
     *
     * @param registrationData данные для регистрации (username и password)
     * @return ResponseEntity с результатом операции
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest registrationData) {
        try {
            // Создаем нового пользователя через сервис
            // Сервис сам проверит, не существует ли уже пользователь с таким именем
            User user = userService.createUser(registrationData.getUsername(), registrationData.getPassword());

            // Создаем ответ для клиента
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Пользователь успешно зарегистрирован");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());

            // Возвращаем успешный ответ со статусом 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            // Если пользователь уже существует, возвращаем ошибку
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            // Возвращаем ошибку со статусом 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для входа пользователя в систему.
     * В упрощенной версии просто проверяем правильность логина/пароля.
     * В реальном приложении здесь бы генерировался JWT токен.
     *
     * @param loginRequest данные для входа (username и password)
     * @return ResponseEntity с результатом аутентификации
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Ищем пользователя по имени в базе данных
        Optional<User> userOptional = userService.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Проверяем, совпадает ли пароль с сохраненным хешем
            // passwordEncoder.matches сравнивает открытый пароль с хешем из БД
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Аутентификация успешна
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Вход выполнен успешно");
                response.put("userId", user.getId());
                response.put("username", user.getUsername());

                return ResponseEntity.ok(response);
            }
        }

        // Если пользователь не найден или пароль неверный
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Неверное имя пользователя или пароль");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}