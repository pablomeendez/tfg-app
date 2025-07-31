package com.tfg.tfg_app.rest.dtos;

import java.time.LocalDateTime;

public class ImageDto {
    private Long id;
    private byte[] imageData;
    private LocalDateTime uploadedAt;

    public ImageDto(Long id, byte[] imageData, LocalDateTime uploadedAt) {
        this.id = id;
        this.imageData = imageData;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
