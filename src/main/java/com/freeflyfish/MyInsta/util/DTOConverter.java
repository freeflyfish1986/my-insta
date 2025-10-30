package com.freeflyfish.MyInsta.util;

import com.freeflyfish.MyInsta.dto.MediaFileDTO;
import com.freeflyfish.MyInsta.dto.PostDTO;
import com.freeflyfish.MyInsta.entity.MediaFile;
import com.freeflyfish.MyInsta.entity.Post;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилитный класс для преобразования Entity объектов в DTO.
 * Отделяет логику преобразования от бизнес-логики сервисов.
 *
 * Почему это важно:
 * 1. Чистая архитектура - сервисы работают с Entity, контроллеры с DTO
 * 2. Переиспользование - один раз написали преобразование, используем везде
 * 3. Тестируемость - легко протестировать преобразование отдельно
 */
@Component
public class DTOConverter {

    /**
     * Преобразует MediaFile entity в MediaFileDTO.
     *
     * @param mediaFile entity объект из базы данных
     * @return DTO объект для отправки клиенту
     */
    public MediaFileDTO convertToMediaFileDTO(MediaFile mediaFile) {
        MediaFileDTO dto = new MediaFileDTO();
        dto.setId(mediaFile.getId());

        // Преобразуем путь к файлу в URL для клиента
        // В реальном приложении здесь бы формировался полный URL с доменом
        dto.setFileUrl("/api/files/" + mediaFile.getFilePath());

        dto.setMediaType(mediaFile.getMediaType());
        dto.setPosition(mediaFile.getPosition());
        return dto;
    }

    /**
     * Преобразует список MediaFile entities в список MediaFileDTO.
     *
     * @param mediaFiles список entity объектов
     * @return список DTO объектов
     */
    public List<MediaFileDTO> convertToMediaFileDTOList(List<MediaFile> mediaFiles) {
        return mediaFiles.stream()
                .map(this::convertToMediaFileDTO)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует Post entity в PostDTO.
     * Включает преобразование всех связанных медиафайлов.
     *
     * @param post entity объект поста
     * @return DTO объект поста
     */
    public PostDTO convertToPostDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setCaption(post.getCaption());
        dto.setCreatedDate(post.getCreatedDate());

        // Добавляем информацию об авторе
        if (post.getUser() != null) {
            dto.setAuthorUsername(post.getUser().getUsername());
            dto.setAuthorId(post.getUser().getId());
        }

        // Преобразуем медиафайлы
        if (post.getMediaFiles() != null) {
            List<MediaFileDTO> mediaFileDTOs = convertToMediaFileDTOList(post.getMediaFiles());
            dto.setMediaFiles(mediaFileDTOs);
        }

        return dto;
    }

    /**
     * Преобразует список Post entities в список PostDTO.
     *
     * @param posts список entity постов
     * @return список DTO постов
     */
    public List<PostDTO> convertToPostDTOList(List<Post> posts) {
        return posts.stream()
                .map(this::convertToPostDTO)
                .collect(Collectors.toList());
    }
}