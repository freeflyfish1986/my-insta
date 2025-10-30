package com.freeflyfish.MyInsta.controller;

import com.freeflyfish.MyInsta.dto.CreatePostRequest;
import com.freeflyfish.MyInsta.dto.PostDTO;
import com.freeflyfish.MyInsta.entity.Post;
import com.freeflyfish.MyInsta.entity.User;
import com.freeflyfish.MyInsta.service.PostService;
import com.freeflyfish.MyInsta.service.UserService;
import com.freeflyfish.MyInsta.util.DTOConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final DTOConverter dtoConverter;

    /**
     * Конструктор для внедрения зависимостей.
     */
    public PostController(PostService postService, UserService userService, DTOConverter dtoConverter) {
        this.postService = postService;
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Эндпоинт для создания нового поста с медиафайлами.
     * Использует multipart/form-data для загрузки файлов.
     *
     * URL: POST /api/posts
     * Формат данных: multipart/form-data
     * Параметры:
     * - title: заголовок поста (текст)
     * - caption: описание поста (текст)
     * - mediaFiles: массив файлов (фото/видео)
     * - authorId: ID автора поста
     *
     * @param title заголовок поста
     * @param caption описание поста
     * @param mediaFiles массив медиафайлов
     * @param authorId ID автора поста
     * @return ResponseEntity с созданным постом или ошибкой
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(
            @RequestParam("title") String title,
            @RequestParam("caption") String caption,
            @RequestParam("mediaFiles") MultipartFile[] mediaFiles,
            @RequestParam("authorId") Long authorId) {

        try {
            // Находим пользователя по ID
            User author = userService.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + authorId + " не найден"));

            // Создаем пост через сервис
            Post post = postService.createPost(author, title, caption, mediaFiles);

            // Преобразуем Entity в DTO для ответа
            PostDTO postDTO = dtoConverter.convertToPostDTO(post);

            // Возвращаем успешный ответ
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Пост успешно создан");
            response.put("post", postDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            // Обрабатываем бизнес-ошибки (валидация, ограничения и т.д.)
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (IOException e) {
            // Обрабатываем ошибки ввода-вывода (проблемы с файлами)
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ошибка при сохранении медиафайлов: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для получения всех постов (лента).
     * Доступен без аутентификации для просмотра.
     *
     * URL: GET /api/posts
     *
     * @return список всех постов в формате DTO
     */
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        try {
            // Получаем все посты через сервис
            List<Post> posts = postService.getAllPosts();

            // Преобразуем Entity в DTO
            List<PostDTO> postDTOs = dtoConverter.convertToPostDTOList(posts);

            return ResponseEntity.ok(postDTOs);

        } catch (Exception e) {
            // В случае ошибки возвращаем пустой список
            return ResponseEntity.ok(List.of());
        }
    }

    /**
     * Эндпоинт для получения поста по ID.
     *
     * URL: GET /api/posts/{id}
     *
     * @param id идентификатор поста
     * @return пост в формате DTO или 404 если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            // Находим пост по ID
            Post post = postService.getPostById(id);

            // Преобразуем Entity в DTO
            PostDTO postDTO = dtoConverter.convertToPostDTO(post);

            return ResponseEntity.ok(postDTO);

        } catch (RuntimeException e) {
            // Если пост не найден
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для получения постов конкретного пользователя.
     *
     * URL: GET /api/posts/user/{userId}
     *
     * @param userId идентификатор пользователя
     * @return список постов пользователя
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUser(@PathVariable Long userId) {
        try {
            // Находим пользователя
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + userId + " не найден"));

            // Получаем посты пользователя
            List<Post> posts = postService.getPostsByUser(user);

            // Преобразуем в DTO
            List<PostDTO> postDTOs = dtoConverter.convertToPostDTOList(posts);

            return ResponseEntity.ok(postDTOs);

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для удаления поста.
     *
     * URL: DELETE /api/posts/{id}
     *
     * @param id идентификатор поста для удаления
     * @return результат операции
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            // Удаляем пост через сервис
            postService.deletePost(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Пост успешно удален");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}