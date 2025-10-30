package com.freeflyfish.MyInsta.dto;

/**
 * DTO для запроса на создание нового комментария.
 * Содержит минимально необходимые данные для создания комментария.
 */
public class CreateCommentRequest {
    private String message; // Текст комментария (обязательное поле)
    private Long authorId; // ID автора комментария (обязательное поле)
    private Long postId; // ID поста, к которому добавляется комментарий (обязательное поле)

    // Конструкторы
    public CreateCommentRequest() {
    }

    public CreateCommentRequest(String message, Long authorId, Long postId) {
        this.message = message;
        this.authorId = authorId;
        this.postId = postId;
    }

    // Геттеры и сеттеры
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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