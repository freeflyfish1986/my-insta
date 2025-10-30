package com.freeflyfish.MyInsta.dto;

import com.freeflyfish.MyInsta.entity.MediaType;

/**
 * DTO для передачи информации о медиафайле клиенту.
 * Содержит только необходимые данные для отображения, без внутренней логики.
 */
public class MediaFileDTO {
    private Long id; // Уникальный идентификатор медиафайла
    private String fileUrl; // URL для доступа к файлу
    private MediaType mediaType; // Тип медиа: PHOTO или VIDEO
    private Integer position; // Порядковый номер в посте (для карусели)

    // Конструкторы
    public MediaFileDTO() {
    }

    public MediaFileDTO(Long id, String fileUrl, MediaType mediaType, Integer position) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.mediaType = mediaType;
        this.position = position;
    }

    // ==================== ГЕТТЕРЫ И СЕТТЕРЫ ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}