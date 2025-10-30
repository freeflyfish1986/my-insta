package com.freeflyfish.MyInsta.entity;

/**
 * Перечисление для определения типа медиафайла.
 * Enum используется для ограничения возможных значений поля типа.
 *
 * PHOTO - изображения (jpg, png, gif, etc.)
 * VIDEO - видеофайлы (mp4, avi, mov, etc.)
 */
public enum MediaType {
    PHOTO,   // Для изображений
    VIDEO    // Для видеофайлов
}