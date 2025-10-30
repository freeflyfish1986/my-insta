package com.freeflyfish.MyInsta.dto;

/**
 * DTO для запроса на создание нового поста.
 * Отдельный класс от PostDTO потому что:
 * - При создании не нужен id (генерируется автоматически)
 * - Не нужны дата создания и автор (устанавливаются автоматически)
 * - Нужно принимать файл изображения (MultipartFile)
 */
public class CreatePostRequest {
    private String title; // Заголовок нового поста
    private String caption; // Описание нового поста

    // Конструкторы
    public CreatePostRequest() {
    }

    public CreatePostRequest(String title, String caption) {
        this.title = title;
        this.caption = caption;
    }

    // Геттеры и сеттеры
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
}