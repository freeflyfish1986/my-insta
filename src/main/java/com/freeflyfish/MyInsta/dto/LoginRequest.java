package com.freeflyfish.MyInsta.dto;

/**
 * DTO (Data Transfer Object) для запроса аутентификации.
 * DTO используются для передачи данных между клиентом и сервером
 * без раскрытия внутренней структуры доменных объектов (Entity).
 *
 * Этот класс представляет данные, которые клиент отправляет при попытке входа.
 */
public class LoginRequest {

    private String username; // Имя пользователя для входа
    private String password; // Пароль пользователя (в открытом виде)

    // Конструктор по умолчанию (обязателен для десериализации JSON)
    public LoginRequest() {
        // Spring использует этот конструктор для создания объекта из JSON
    }

    // Конструктор со всеми полями (удобно для создания объектов в коде)
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры (обязательны для работы Spring)

    /**
     * @return текущее имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя
     * @param username новое имя пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return текущий пароль (в открытом виде)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль
     * @param password новый пароль (в открытом виде)
     */
    public void setPassword(String password) {
        this.password = password;
    }
}