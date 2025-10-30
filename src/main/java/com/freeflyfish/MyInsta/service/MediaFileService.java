package com.freeflyfish.MyInsta.service;

import com.freeflyfish.MyInsta.entity.MediaFile;
import com.freeflyfish.MyInsta.entity.MediaType;
import com.freeflyfish.MyInsta.repository.MediaFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MediaFileService {

    private final MediaFileRepository mediaFileRepository;

    // Папка для хранения загруженных файлов
    private final String UPLOAD_DIR = "uploads/";

    // Разрешенные расширения для фото
    private final List<String> ALLOWED_PHOTO_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    // Разрешенные расширения для видео
    private final List<String> ALLOWED_VIDEO_EXTENSIONS = Arrays.asList(
            "mp4", "avi", "mov", "wmv", "flv", "webm"
    );

    public MediaFileService(MediaFileRepository mediaFileRepository) {
        this.mediaFileRepository = mediaFileRepository;
    }

    /**
     * Определяет тип медиафайла на основе расширения.
     *
     * @param originalFileName оригинальное имя файла
     * @return MediaType.PHOTO или MediaType.VIDEO
     * @throws RuntimeException если расширение не поддерживается
     */
    public MediaType determineMediaType(String originalFileName) {
        // Извлекаем расширение файла (после последней точки)
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

        if (ALLOWED_PHOTO_EXTENSIONS.contains(extension)) {
            return MediaType.PHOTO;
        } else if (ALLOWED_VIDEO_EXTENSIONS.contains(extension)) {
            return MediaType.VIDEO;
        } else {
            throw new RuntimeException("Неподдерживаемый тип файла: " + extension +
                    ". Разрешенные фото: " + ALLOWED_PHOTO_EXTENSIONS +
                    ", видео: " + ALLOWED_VIDEO_EXTENSIONS);
        }
    }

    /**
     * Сохраняет медиафайл на диск и создает запись в базе данных.
     *
     * @param file загружаемый файл
     * @param mediaType тип медиа (фото/видео)
     * @return сохраненный MediaFile объект
     * @throws IOException если произошла ошибка при сохранении файла
     */
    public MediaFile saveMediaFile(MultipartFile file, MediaType mediaType) throws IOException {
        // Генерируем уникальное имя файла чтобы избежать конфликтов
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Создаем подпапку в зависимости от типа медиа
        String subfolder = mediaType == MediaType.PHOTO ? "photos/" : "videos/";
        String fullUploadPath = UPLOAD_DIR + subfolder;

        // Создаем папку если она не существует
        Path uploadPath = Paths.get(fullUploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Сохраняем файл на диск
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        // Создаем и сохраняем запись в базе данных
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFilePath(fullUploadPath + uniqueFileName); // Полный путь к файлу
        mediaFile.setMediaType(mediaType);

        return mediaFileRepository.save(mediaFile);
    }

    /**
     * Подсчитывает количество медиафайлов в посте.
     *
     * @param postId идентификатор поста
     * @return количество медиафайлов
     */
    public long countMediaFilesInPost(Long postId) {
        return mediaFileRepository.countByPostId(postId);
    }
}