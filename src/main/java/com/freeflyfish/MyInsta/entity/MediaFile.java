package com.freeflyfish.MyInsta.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Сущность для хранения информации о медиафайлах (фото и видео).
 * Отдельная таблица нужна потому что:
 * 1. Один пост может содержать несколько медиафайлов
 * 2. Нужно хранить тип файла (фото/видео)
 * 3. Легче управлять ограничениями количества файлов
 */
@Entity
@Data
@Table(name = "media_files")
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Путь к файлу на сервере.
     * Например: "uploads/photos/abc123.jpg"
     */
    @Column(name = "file_path", nullable = false)
    private String filePath;

    /**
     * Тип медиафайла: PHOTO или VIDEO.
     * Определяет как обрабатывать и отображать файл.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;

    /**
     * Порядковый номер файла в посте.
     * Используется для определения порядка отображения медиа.
     */
    @Column(name = "position")
    private Integer position;

    /**
     * Связь с постом: многие медиафайлы принадлежат одному посту.
     * FetchType.LAZY - файлы загружаются только когда к ним обращаются
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}