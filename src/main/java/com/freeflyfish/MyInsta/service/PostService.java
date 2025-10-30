package com.freeflyfish.MyInsta.service;

import com.freeflyfish.MyInsta.entity.Post;
import com.freeflyfish.MyInsta.entity.User;
import com.freeflyfish.MyInsta.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;

    // Папка для хранения загруженных изображений
    private final String UPLOAD_DIR = "uploads/";

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    /**
     * Создание нового поста
     * Сохраняем изображение на диск и записываем путь в БД
     */
    public Post createPost(User user, String title, String caption, MultipartFile imageFile) throws IOException {
        // Генерируем уникальное имя файла чтобы избежать конфликтов
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

        // Создаем папку uploads если она не существует
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Сохраняем файл на диск
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), filePath);

        // Создаем пост и сохраняем в БД
        Post post = new Post();
        post.setTitle(title);
        post.setCaption(caption);
        post.setImagePath(UPLOAD_DIR + fileName); // Сохраняем относительный путь к файлу
        post.setUser(user);

        return postRepository.save(post);
    }

    /**
     * Получение всех постов (для ленты)
     * Сортировка по дате - новые первыми
     */
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    /**
     * Получение постов конкретного пользователя
     */
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUserIdOrderByCreatedDateDesc(user.getId());
    }

    /**
     * Получение поста по ID
     */
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * Удаление поста
     */
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}