package com.freeflyfish.MyInsta.dto;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных о комментарии между клиентом и сервером.
 * Содержит информацию о комментарии и его авторе.
 */
public class CommentDTO {
    private Long id; // Уникальный идентификатор комментария
    private String message; // Текст комментария
    private LocalDateTime createdDate; // Дата и время создания комментария
    private String authorUsername; // Имя пользователя автора комментария
    private Long authorId; // ID автора комментария
    private Long postId; // ID поста, к которому относится комментарий

    // Конструкторы
    public CommentDTO() {
    }

    public CommentDTO(Long id, String message, LocalDateTime createdDate,
                      String authorUsername, Long authorId, Long postId) {
        this.id = id;
        this.message = message;
        this.createdDate = createdDate;
        this.authorUsername = authorUsername;
        this.authorId = authorId;
        this.postId = postId;
    }

    // ==================== ГЕТТЕРЫ И СЕТТЕРЫ ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}