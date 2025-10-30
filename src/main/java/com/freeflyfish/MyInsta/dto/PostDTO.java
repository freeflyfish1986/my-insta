package com.freeflyfish.MyInsta.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Обновленный DTO для поста с поддержкой множественных медиафайлов.
 */
public class PostDTO {
    private Long id;
    private String title;
    private String caption;
    private LocalDateTime createdDate;
    private String authorUsername;
    private Long authorId;

    // Заменяем одиночное изображение на список медиафайлов
    private List<MediaFileDTO> mediaFiles = new ArrayList<>();

    // Конструкторы
    public PostDTO() {
    }

    public PostDTO(Long id, String title, String caption, LocalDateTime createdDate,
                   String authorUsername, Long authorId, List<MediaFileDTO> mediaFiles) {
        this.id = id;
        this.title = title;
        this.caption = caption;
        this.createdDate = createdDate;
        this.authorUsername = authorUsername;
        this.authorId = authorId;
        this.mediaFiles = mediaFiles;
    }

    // ==================== ГЕТТЕРЫ И СЕТТЕРЫ ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    /**
     * @return список медиафайлов поста (фото и видео)
     */
    public List<MediaFileDTO> getMediaFiles() {
        return mediaFiles;
    }

    /**
     * Устанавливает список медиафайлов
     * @param mediaFiles новый список медиафайлов
     */
    public void setMediaFiles(List<MediaFileDTO> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }
}