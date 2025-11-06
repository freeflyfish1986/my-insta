package com.freeflyfish.MyInsta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для всего приложения
 * Ловит все необработанные исключения и логирует их
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает все исключения RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        System.out.println("=== ГЛОБАЛЬНЫЙ ОБРАБОТЧИК: RuntimeException ===");
        System.out.println("Сообщение: " + e.getMessage());
        System.out.println("Класс: " + e.getClass().getSimpleName());
        e.printStackTrace();

        Map<String, String> response = new HashMap<>();
        response.put("error", "RuntimeException: " + e.getMessage());
        response.put("type", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Обрабатывает все остальные исключения
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        System.out.println("=== ГЛОБАЛЬНЫЙ ОБРАБОТЧИК: Exception ===");
        System.out.println("Сообщение: " + e.getMessage());
        System.out.println("Класс: " + e.getClass().getSimpleName());
        e.printStackTrace();

        Map<String, String> response = new HashMap<>();
        response.put("error", "Exception: " + e.getMessage());
        response.put("type", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}