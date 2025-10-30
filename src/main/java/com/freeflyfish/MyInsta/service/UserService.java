package com.freeflyfish.MyInsta.service;

import com.freeflyfish.MyInsta.entity.User;
import com.freeflyfish.MyInsta.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Внедрение зависимостей через конструктор - это современный и рекомендуемый подход
     * Spring автоматически найдет соответствующие бины и передаст их в конструктор
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Создание нового пользователя
     * Шифруем пароль перед сохранением в БД
     */
    public User createUser(String username, String password) {
        // Проверяем, не существует ли уже пользователь с таким username
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(username);
        // Шифруем пароль для безопасности
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    /**
     * Поиск пользователя по username
     * Используется для аутентификации
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Получение пользователя по ID
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}