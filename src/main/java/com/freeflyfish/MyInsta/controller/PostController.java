package com.freeflyfish.MyInsta.controller;

import com.freeflyfish.MyInsta.dto.PostDTO;
import com.freeflyfish.MyInsta.dto.TestPostRequest;
import com.freeflyfish.MyInsta.entity.MediaFile;
import com.freeflyfish.MyInsta.entity.MediaType;
import com.freeflyfish.MyInsta.entity.Post;
import com.freeflyfish.MyInsta.entity.User;
import com.freeflyfish.MyInsta.repository.MediaFileRepository;
import com.freeflyfish.MyInsta.repository.PostRepository;
import com.freeflyfish.MyInsta.service.MediaFileService;
import com.freeflyfish.MyInsta.service.PostService;
import com.freeflyfish.MyInsta.service.UserService;
import com.freeflyfish.MyInsta.util.DTOConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final DTOConverter dtoConverter;
    private final PostRepository postRepository;
    private final MediaFileService mediaFileService;
    private final MediaFileRepository mediaFileRepository;

    public PostController(PostService postService, UserService userService,
                          DTOConverter dtoConverter, PostRepository postRepository,
                          MediaFileService mediaFileService, MediaFileRepository mediaFileRepository) {
        this.postService = postService;
        this.userService = userService;
        this.dtoConverter = dtoConverter;
        this.postRepository = postRepository;
        this.mediaFileService = mediaFileService;
        this.mediaFileRepository = mediaFileRepository;

        System.out.println("=== PostController создан ===");
        System.out.println("userService: " + (userService != null ? "NOT NULL" : "NULL"));
        System.out.println("postService: " + (postService != null ? "NOT NULL" : "NULL"));
        System.out.println("dtoConverter: " + (dtoConverter != null ? "NOT NULL" : "NULL"));
        System.out.println("postRepository: " + (postRepository != null ? "NOT NULL" : "NULL"));
        System.out.println("mediaFileService: " + (mediaFileService != null ? "NOT NULL" : "NULL"));
        System.out.println("mediaFileRepository: " + (mediaFileRepository != null ? "NOT NULL" : "NULL"));
    }

    /**
     * Простейший эндпоинт для проверки что контроллер работает
     */
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "PostController работает!");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    /**
     * Эндпоинт для тестирования создания поста БЕЗ файлов
     */
    @PostMapping("/test")
    public ResponseEntity<?> createTestPost(@RequestBody TestPostRequest request) {
        try {
            System.out.println("=== СОЗДАНИЕ ТЕСТОВОГО ПОСТА ===");
            System.out.println("Title: " + request.getTitle());
            System.out.println("Caption: " + request.getCaption());
            System.out.println("AuthorId: " + request.getAuthorId());

            // ШАГ 1: Поиск пользователя
            System.out.println("ШАГ 1: Поиск пользователя...");
            User author = userService.findById(request.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            System.out.println("✓ Пользователь найден: " + author.getUsername());

            // ШАГ 2: Создание объекта Post
            System.out.println("ШАГ 2: Создание объекта Post...");
            Post post = new Post();
            post.setTitle(request.getTitle());
            post.setCaption(request.getCaption());
            post.setUser(author);
            System.out.println("✓ Объект Post создан");

            // ШАГ 3: Сохранение в БД
            System.out.println("ШАГ 3: Сохранение Post в БД...");
            Post savedPost = postRepository.save(post);
            System.out.println("✓ Post сохранен с ID: " + savedPost.getId());

            // Успешный ответ
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Пост успешно создан!");
            response.put("postId", savedPost.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.out.println("❌ ОШИБКА В createTestPost: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Эндпоинт для создания нового поста с медиафайлами.
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(
            @RequestParam("title") String title,
            @RequestParam("caption") String caption,
            @RequestParam("mediaFiles") MultipartFile[] mediaFiles,
            @RequestParam("authorId") Long authorId) {

        try {
            User author = userService.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + authorId + " не найден"));

            Post post = postService.createPost(author, title, caption, mediaFiles);
            PostDTO postDTO = dtoConverter.convertToPostDTO(post);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Пост успешно создан");
            response.put("post", postDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ошибка при сохранении медиафайлов: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Получение всех постов для ленты.
     */
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();
            List<PostDTO> postDTOs = dtoConverter.convertToPostDTOList(posts);
            return ResponseEntity.ok(postDTOs);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    /**
     * Получение поста по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            Post post = postService.getPostById(id);
            PostDTO postDTO = dtoConverter.convertToPostDTO(post);
            return ResponseEntity.ok(postDTO);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Получение постов конкретного пользователя.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUser(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + userId + " не найден"));

            List<Post> posts = postService.getPostsByUser(user);
            List<PostDTO> postDTOs = dtoConverter.convertToPostDTOList(posts);
            return ResponseEntity.ok(postDTOs);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Удаление поста.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
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

    /**
     * Самый простой эндпоинт для диагностики - без параметров
     */
    @PostMapping("/simple")
    public ResponseEntity<?> simpleTest() {
        System.out.println("=== ПРОСТЕЙШИЙ POST ВЫЗВАН ===");
        System.out.println("Этот метод должен работать всегда!");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Простейший POST работает");
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }

    /**
     * Эндпоинт с простым телом как String
     */
    @PostMapping("/simple-body")
    public ResponseEntity<?> simpleBodyTest(@RequestBody String rawBody) {
        System.out.println("=== POST С ТЕЛОМ ВЫЗВАН ===");
        System.out.println("Полученное тело: " + rawBody);
        System.out.println("Длина тела: " + rawBody.length());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "POST с телом работает");
        response.put("body", rawBody);
        response.put("bodyLength", rawBody.length());

        return ResponseEntity.ok(response);
    }

    /**
     * Эндпоинт с DTO но без бизнес-логики
     */
    @PostMapping("/test-dto")
    public ResponseEntity<?> testDto(@RequestBody TestPostRequest request) {
        System.out.println("=== TEST DTO ВЫЗВАН ===");
        System.out.println("Title: " + request.getTitle());
        System.out.println("Caption: " + request.getCaption());
        System.out.println("AuthorId: " + request.getAuthorId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "DTO получен успешно");
        response.put("title", request.getTitle());
        response.put("caption", request.getCaption());
        response.put("authorId", request.getAuthorId());

        return ResponseEntity.ok(response);
    }

    /**
     * Упрощенный эндпоинт для тестирования загрузки файла (без сохранения в БД)
     */
    @PostMapping(value = "/test-simple-file", consumes = "multipart/form-data")
    public ResponseEntity<?> testSimpleFile(
            @RequestParam("title") String title,
            @RequestParam("caption") String caption,
            @RequestParam("authorId") Long authorId,
            @RequestParam("mediaFile") MultipartFile mediaFile) {

        try {
            System.out.println("=== ТЕСТ ЗАГРУЗКИ ФАЙЛА ===");
            System.out.println("Title: " + title);
            System.out.println("Caption: " + caption);
            System.out.println("AuthorId: " + authorId);
            System.out.println("File: " + mediaFile.getOriginalFilename() + " (" + mediaFile.getSize() + " bytes)");

            // Просто сохраняем файл на диск БЕЗ сохранения в БД
            MediaType mediaType = mediaFileService.determineMediaType(mediaFile.getOriginalFilename());
            String filePath = saveFileToDisk(mediaFile, mediaType);

            System.out.println("Файл сохранен на диск: " + filePath);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Файл загружен успешно!");
            response.put("filePath", filePath);
            response.put("mediaType", mediaType.toString());
            response.put("savedToDisk", true);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("ОШИБКА: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Рабочий эндпоинт для создания поста с файлом (исправленная версия)
     */
    @PostMapping(value = "/create-with-file", consumes = "multipart/form-data")
    public ResponseEntity<?> createPostWithFile(
            @RequestParam("title") String title,
            @RequestParam("caption") String caption,
            @RequestParam("authorId") Long authorId,
            @RequestParam("mediaFile") MultipartFile mediaFile) {

        try {
            System.out.println("=== СОЗДАНИЕ ПОСТА С ФАЙЛОМ (ИСПРАВЛЕННАЯ ВЕРСИЯ) ===");
            System.out.println("Title: " + title);
            System.out.println("Caption: " + caption);
            System.out.println("AuthorId: " + authorId);
            System.out.println("File: " + mediaFile.getOriginalFilename());

            // Находим пользователя
            User author = userService.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            // 1. Создаем и сохраняем пост
            Post post = new Post();
            post.setTitle(title);
            post.setCaption(caption);
            post.setUser(author);

            Post savedPost = postRepository.save(post);
            System.out.println("✓ Пост сохранен с ID: " + savedPost.getId());

            // 2. Сохраняем файл на диск
            MediaType mediaType = mediaFileService.determineMediaType(mediaFile.getOriginalFilename());
            String filePath = saveFileToDisk(mediaFile, mediaType);
            System.out.println("✓ Файл сохранен на диск: " + filePath);

            // 3. Создаем MediaFile и устанавливаем связь с постом
            MediaFile mediaFileEntity = new MediaFile();
            mediaFileEntity.setFilePath(filePath);
            mediaFileEntity.setMediaType(mediaType);
            mediaFileEntity.setPost(savedPost);  // ВАЖНО: устанавливаем связь
            mediaFileEntity.setPosition(0);

            MediaFile savedMediaFile = mediaFileRepository.save(mediaFileEntity);
            System.out.println("✓ MediaFile сохранен с ID: " + savedMediaFile.getId());

            // 4. Обновляем пост с медиафайлом
            savedPost.getMediaFiles().add(savedMediaFile);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Пост с файлом создан успешно!");
            response.put("postId", savedPost.getId());
            response.put("mediaFileId", savedMediaFile.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.out.println("ОШИБКА: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Простейший тест создания поста с файлом
     */
    @PostMapping("/create-simple-file")
    public ResponseEntity<?> createSimpleFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== ПРОСТЕЙШИЙ СОЗДАНИЕ С ФАЙЛОМ ===");
            System.out.println("File: " + file.getOriginalFilename());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Простейший метод с файлом работает!");
            response.put("fileName", file.getOriginalFilename());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("ОШИБКА: " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Создание тестового пользователя
     */
    @PostMapping("/create-test-user")
    public ResponseEntity<?> createTestUser() {
        try {
            System.out.println("=== СОЗДАНИЕ ТЕСТОВОГО ПОЛЬЗОВАТЕЛЯ ===");

            User testUser = userService.createUser("testuser", "password123");

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Тестовый пользователь создан");
            response.put("userId", testUser.getId());
            response.put("username", testUser.getUsername());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("ОШИБКА: " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Вспомогательный метод для сохранения файла на диск
     */
    private String saveFileToDisk(MultipartFile file, MediaType mediaType) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        String subfolder = mediaType == MediaType.PHOTO ? "photos/" : "videos/";
        String fullUploadPath = "uploads/" + subfolder;

        Path uploadPath = Paths.get(fullUploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        return fullUploadPath + uniqueFileName;
    }
}