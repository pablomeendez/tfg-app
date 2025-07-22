package com.tfg.tfg_app.rest.dtos;

import java.time.LocalDateTime;
import java.util.Set;

import com.tfg.tfg_app.model.entities.Images;

public class DiaryEntryDto {
    
    private String description;
    private LocalDateTime date;
    private Set<Images> images;
    private Long moodId;

    public DiaryEntryDto() {
    }

    public DiaryEntryDto(String description, LocalDateTime date, Set<Images> images, Long moodId) {
        this.description = description;
        this.date = date;
        this.images = images;
        this.moodId = moodId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<Images> getImages() {
        return images;
    }

    public void setImages(Set<Images> images) {
        this.images = images;
    }

    public Long getMoodId() {
        return moodId;
    }

    public void setMoodId(Long moodId) {
        this.moodId = moodId;
    }
}
