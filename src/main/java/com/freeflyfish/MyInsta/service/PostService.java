package com.freeflyfish.MyInsta.service;

import com.freeflyfish.MyInsta.entity.MediaFile;
import com.freeflyfish.MyInsta.entity.MediaType;
import com.freeflyfish.MyInsta.entity.Post;
import com.freeflyfish.MyInsta.entity.User;
import com.freeflyfish.MyInsta.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MediaFileService mediaFileService;

    // Максимальное количество медиафайлов в одном посте
    private static final int MAX_MEDIA_FILES_PER_POST = 20;

    /**
     * Конструктор для внедрения зависимостей.
     * PostService теперь зависит от MediaFileService для работы с медиафайлами.
     */
    public PostService(PostRepository postRepository, MediaFileService mediaFileService) {
        this.postRepository = postRepository;
        this.mediaFileService = mediaFileService;
    }

    /**
     * Создает новый пост с несколькими медиафайлами (фото/видео).
     *
     * @param user автор поста
     * @param title заголовок поста
     * @param caption описание поста
     * @param mediaFiles массив загруженных медиафайлов
     * @return сохраненный пост
     * @throws IOException если произошла ошибка при сохранении файлов
     * @throws RuntimeException если превышено максимальное количество файлов
     */
    public Post createPost(User user, String title, String caption, MultipartFile[] mediaFiles) throws IOException {
        // Проверяем, не превышено ли максимальное количество файлов
        if (mediaFiles.length > MAX_MEDIA_FILES_PER_POST) {
            throw new RuntimeException("Превышено максимальное количество медиафайлов. Максимум: " + MAX_MEDIA_FILES_PER_POST);
        }

        // Проверяем что есть хотя бы один медиафайл
        if (mediaFiles.length == 0) {
            throw new RuntimeException("Пост должен содержать хотя бы один медиафайл");
        }

        // Проверяем что первый файл не null и имеет имя (Spring иногда передает пустые файлы)
        if (mediaFiles[0] == null || mediaFiles[0].isEmpty()) {
            throw new RuntimeException("Первый медиафайл не может быть пустым");
        }

        // Создаем новый пост
        Post post = new Post();
        post.setTitle(title);
        post.setCaption(caption);
        post.setUser(user);

        // Обрабатываем и добавляем каждый медиафайл
        for (MultipartFile mediaFile : mediaFiles) {
            // Пропускаем пустые файлы (Spring может передавать null в массиве)
            if (mediaFile == null || mediaFile.isEmpty()) {
                continue;
            }

            // Определяем тип медиа (фото/видео) на основе расширения файла
            MediaType mediaType = mediaFileService.determineMediaType(mediaFile.getOriginalFilename());

            // Сохраняем медиафайл на диск и получаем объект MediaFile
            MediaFile savedMediaFile = mediaFileService.saveMediaFile(mediaFile, mediaType);

            // Добавляем медиафайл к посту (внутри метода addMediaFile проверяется ограничение в 20 файлов)
            post.addMediaFile(savedMediaFile);
        }

        // Проверяем что после обработки остался хотя бы один валидный файл
        if (post.getMediaFiles().isEmpty()) {
            throw new RuntimeException("Не удалось загрузить ни одного валидного медиафайла");
        }

        // Сохраняем пост в базу данных (каскадно сохраняются и медиафайлы)
        return postRepository.save(post);
    }

    /**
     * Получение всех постов для ленты.
     * Посты возвращаются отсортированными по дате создания (новые первыми).
     *
     * @return список всех постов
     */
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    /**
     * Получение постов конкретного пользователя.
     *
     * @param user пользователь, чьи посты нужно найти
     * @return список постов пользователя
     */
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUserIdOrderByCreatedDateDesc(user.getId());
    }

    /**
     * Получение поста по его идентификатору.
     *
     * @param id идентификатор поста
     * @return Optional с постом, если найден
     */
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пост с ID " + id + " не найден"));
    }

    /**
     * Удаление поста по идентификатору.
     * При каскадном удалении также удаляются все связанные медиафайлы и комментарии.
     *
     * @param postId идентификатор поста для удаления
     */
    public void deletePost(Long postId) {
        // Проверяем существует ли пост перед удалением
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Пост с ID " + postId + " не найден");
        }
        postRepository.deleteById(postId);
    }

    /**
     * Получение количества постов пользователя.
     *
     * @param user пользователь
     * @return количество постов
     */
    public long getPostCountByUser(User user) {
        return postRepository.findByUserIdOrderByCreatedDateDesc(user.getId()).size();
    }
}