package com.freeflyfish.MyInsta.dto;

/**
 * Простой DTO для тестового запроса
 */
public class TestPostRequest {
    private String title;
    private String caption;
    private Long authorId;

    // Конструктор по умолчанию (ОБЯЗАТЕЛЕН для Jackson)
    public TestPostRequest() {
    }

    // Геттеры и сеттеры (ОБЯЗАТЕЛЬНЫ для Jackson)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}