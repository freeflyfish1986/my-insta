package com.freeflyfish.MyInsta.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String title;

    @Column(length = 5000)
    private String caption;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    // Заменяем одно изображение на коллекцию медиафайлов
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFile> mediaFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    /**
     * Вспомогательный метод для добавления медиафайла с проверкой ограничения
     * @param mediaFile добавляемый медиафайл
     * @throws RuntimeException если превышено максимальное количество файлов (20)
     */
    public void addMediaFile(MediaFile mediaFile) {
        if (this.mediaFiles.size() >= 20) {
            throw new RuntimeException("Максимальное количество медиафайлов в посте: 20");
        }
        mediaFile.setPost(this);
        mediaFile.setPosition(this.mediaFiles.size()); // Устанавливаем порядковый номер
        this.mediaFiles.add(mediaFile);
    }
    // ========== ГЕТТЕРЫ И СЕТТЕРЫ ==========

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<MediaFile> getMediaFiles() {
        return this.mediaFiles;
    }

    public void setMediaFiles(List<MediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}