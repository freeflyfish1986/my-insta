package com.freeflyfish.MyInsta.controller;

import com.freeflyfish.MyInsta.dto.CommentDTO;
import com.freeflyfish.MyInsta.dto.CreateCommentRequest;
import com.freeflyfish.MyInsta.entity.Comment;
import com.freeflyfish.MyInsta.entity.Post;
import com.freeflyfish.MyInsta.entity.User;
import com.freeflyfish.MyInsta.service.CommentService;
import com.freeflyfish.MyInsta.service.PostService;
import com.freeflyfish.MyInsta.service.UserService;
import com.freeflyfish.MyInsta.util.DTOConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;
    private final DTOConverter dtoConverter;

    /**
     * Конструктор для внедрения зависимостей.
     */
    public CommentController(CommentService commentService, UserService userService,
                             PostService postService, DTOConverter dtoConverter) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Эндпоинт для создания нового комментария.
     *
     * URL: POST /api/comments
     * Формат данных: application/json
     *
     * @param createCommentRequest данные для создания комментария
     * @return ResponseEntity с созданным комментарием или ошибкой
     */
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest createCommentRequest) {
        try {
            // Валидация входных данных
            if (createCommentRequest.getMessage() == null || createCommentRequest.getMessage().trim().isEmpty()) {
                throw new RuntimeException("Текст комментария не может быть пустым");
            }

            if (createCommentRequest.getAuthorId() == null) {
                throw new RuntimeException("ID автора обязателен");
            }

            if (createCommentRequest.getPostId() == null) {
                throw new RuntimeException("ID поста обязателен");
            }

            // Находим автора комментария
            User author = userService.findById(createCommentRequest.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + createCommentRequest.getAuthorId() + " не найден"));

            // Находим пост, к которому добавляется комментарий
            Post post = postService.getPostById(createCommentRequest.getPostId());

            // Создаем комментарий через сервис
            Comment comment = commentService.createComment(author, post, createCommentRequest.getMessage());

            // Преобразуем Entity в DTO для ответа
            CommentDTO commentDTO = dtoConverter.convertToCommentDTO(comment);

            // Возвращаем успешный ответ
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Комментарий успешно добавлен");
            response.put("comment", commentDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            // Обрабатываем бизнес-ошибки
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для получения всех комментариев поста.
     *
     * URL: GET /api/comments/post/{postId}
     *
     * @param postId идентификатор поста
     * @return список комментариев поста
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPost(@PathVariable Long postId) {
        try {
            // Находим пост
            Post post = postService.getPostById(postId);

            // Получаем комментарии поста через сервис
            List<Comment> comments = commentService.getCommentsByPost(post);

            // Преобразуем в DTO
            List<CommentDTO> commentDTOs = dtoConverter.convertToCommentDTOList(comments);

            return ResponseEntity.ok(commentDTOs);

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для получения комментариев пользователя.
     *
     * URL: GET /api/comments/user/{userId}
     *
     * @param userId идентификатор пользователя
     * @return список комментариев пользователя
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCommentsByUser(@PathVariable Long userId) {
        try {
            // Находим пользователя
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + userId + " не найден"));

            // Получаем комментарии пользователя через сервис
            List<Comment> comments = commentService.getCommentsByUser(user);

            // Преобразуем в DTO
            List<CommentDTO> commentDTOs = dtoConverter.convertToCommentDTOList(comments);

            return ResponseEntity.ok(commentDTOs);

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для удаления комментария.
     *
     * URL: DELETE /api/comments/{id}
     *
     * @param id идентификатор комментария для удаления
     * @return результат операции
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            // Удаляем комментарий через сервис
            commentService.deleteComment(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Комментарий успешно удален");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для получения количества комментариев поста.
     *
     * URL: GET /api/comments/post/{postId}/count
     *
     * @param postId идентификатор поста
     * @return количество комментариев
     */
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<?> getCommentCountByPost(@PathVariable Long postId) {
        try {
            // Находим пост
            Post post = postService.getPostById(postId);

            // Получаем комментарии и считаем их
            List<Comment> comments = commentService.getCommentsByPost(post);
            int commentCount = comments.size();

            Map<String, Object> response = new HashMap<>();
            response.put("postId", postId);
            response.put("commentCount", commentCount);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}