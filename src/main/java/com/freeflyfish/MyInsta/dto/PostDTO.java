package com.freeflyfish.MyInsta.dto;

import java.time.LocalDateTime;

/**
 * DTO для передачи данных о посте между клиентом и сервером.
 * Используется вместо Entity класса Post чтобы:
 * 1. Не раскрывать внутреннюю структуру базы данных
 * 2. Контролировать какие данные отправляются клиенту
 * 3. Избежать циклических ссылок при сериализации JSON
 */
public class PostDTO {
    private Long id; // Уникальный идентификатор поста
    private String title; // Заголовок поста
    private String caption; // Описание/подпись к посту
    private String imageUrl; // URL для доступа к изображению
    private LocalDateTime createdDate; // Дата создания поста
    private String authorUsername; // Имя автора поста (только для чтения)
    private Long authorId; // ID автора поста

    // Конструктор по умолчанию (обязателен для Spring)
    public PostDTO() {
    }

    // Конструктор для удобного создания DTO
    public PostDTO(Long id, String title, String caption, String imageUrl,
                   LocalDateTime createdDate, String authorUsername, Long authorId) {
        this.id = id;
        this.title = title;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.authorUsername = authorUsername;
        this.authorId = authorId;
    }

    // ==================== ГЕТТЕРЫ ====================

    /**
     * @return уникальный идентификатор поста в базе данных
     */
    public Long getId() {
        return id;
    }

    /**
     * @return заголовок поста (может быть null)
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return подробное описание или подпись к посту
     */
    public String getCaption() {
        return caption;
    }

    /**
     * @return относительный путь к файлу изображения на сервере
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @return дата и время создания поста (устанавливается автоматически)
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * @return имя пользователя автора поста (только для чтения)
     */
    public String getAuthorUsername() {
        return authorUsername;
    }

    /**
     * @return идентификатор автора поста
     */
    public Long getAuthorId() {
        return authorId;
    }

    // ==================== СЕТТЕРЫ ====================

    /**
     * Устанавливает идентификатор поста
     * @param id новый идентификатор
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Устанавливает заголовок поста
     * @param title новый заголовок
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Устанавливает описание поста
     * @param caption новое описание
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Устанавливает путь к изображению
     * @param imageUrl новый путь к изображению
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Устанавливает дату создания
     * @param createdDate новая дата создания
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Устанавливает имя автора
     * @param authorUsername новое имя автора
     */
    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    /**
     * Устанавливает идентификатор автора
     * @param authorId новый идентификатор автора
     */
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}