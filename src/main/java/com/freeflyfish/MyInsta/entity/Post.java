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

    // Здесь будет храниться путь к изображению на диске
    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    // Связь: многие постов принадлежат одному пользователю
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Связь: один пост - много комментариев
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}