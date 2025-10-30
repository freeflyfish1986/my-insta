package com.freeflyfish.MyInsta.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    /**
     * Эндпоинт для отдачи медиафайлов (фото и видео) клиенту.
     * Этот эндпоинт преобразует путь из базы данных в реальный файл на диске.
     *
     * @param filePath путь к файлу относительно папки uploads (передается в URL)
     * @return ResponseEntity с файлом или ошибкой 404
     */
    @GetMapping("/{filePath:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filePath) {
        try {
            // Создаем полный путь к файлу на диске
            // filePath приходит в формате "photos/abc123.jpg" или "videos/xyz789.mp4"
            Path file = Paths.get("uploads/" + filePath);

            // Проверяем существует ли файл
            if (!Files.exists(file)) {
                return ResponseEntity.notFound().build();
            }

            // Создаем Resource объект для файла
            Resource resource = new UrlResource(file.toUri());

            // Определяем Content-Type на основе расширения файла
            String contentType = determineContentType(filePath);

            // Возвращаем файл с правильными заголовками
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            // В случае ошибки возвращаем 404
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Определяет Content-Type файла на основе его расширения.
     * Это нужно чтобы браузер правильно отображал фото и видео.
     *
     * @param filename имя файла или путь
     * @return строку с MIME-типом
     */
    private String determineContentType(String filename) {
        // Извлекаем расширение файла (в нижнем регистре)
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        // Сопоставляем расширения с MIME-типами
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            case "mp4":
                return "video/mp4";
            case "avi":
                return "video/x-msvideo";
            case "mov":
                return "video/quicktime";
            case "wmv":
                return "video/x-ms-wmv";
            case "flv":
                return "video/x-flv";
            case "webm":
                return "video/webm";
            default:
                return "application/octet-stream"; // Бинарный файл по умолчанию
        }
    }
}