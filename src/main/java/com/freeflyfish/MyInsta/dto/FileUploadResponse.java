package com.freeflyfish.MyInsta.dto;

/**
 * DTO для ответа с информацией о загруженном файле
 */
public class FileUploadResponse {
    private String message;
    private String filePath;
    private String mediaType;
    private Long fileId;

    // Конструкторы, геттеры, сеттеры
    public FileUploadResponse() {}

    public FileUploadResponse(String message, String filePath, String mediaType, Long fileId) {
        this.message = message;
        this.filePath = filePath;
        this.mediaType = mediaType;
        this.fileId = fileId;
    }

    // Геттеры и сеттеры
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public Long getFileId() { return fileId; }
    public void setFileId(Long fileId) { this.fileId = fileId; }
}