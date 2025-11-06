package com.freeflyfish.MyInsta.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    // Связь: один пользователь - много постов
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    // Связь: один пользователь - много комментариев
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // Автоматически устанавливаем дату создания перед сохранением
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}