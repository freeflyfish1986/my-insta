package com.freeflyfish.MyInsta.repository;

import com.freeflyfish.MyInsta.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

    /**
     * Находит все медиафайлы поста, отсортированные по позиции
     * @param postId идентификатор поста
     * @return список медиафайлов в порядке отображения
     */
    List<MediaFile> findByPostIdOrderByPositionAsc(Long postId);

    /**
     * Подсчитывает количество медиафайлов в посте
     * @param postId идентификатор поста
     * @return количество медиафайлов
     */
    long countByPostId(Long postId);
}