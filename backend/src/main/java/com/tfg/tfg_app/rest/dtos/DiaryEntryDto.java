package com.tfg.tfg_app.rest.dtos;

import java.util.List;
import java.util.Set;

import com.tfg.tfg_app.model.entities.Images;

public class DiaryEntryDto {
    
    private String description;
    private List<byte[]> images;
    private Long moodId;

    public DiaryEntryDto() {
    }

    public DiaryEntryDto(String description, List<byte[]> images, Long moodId) {
        this.description = description;
        this.images = images;
        this.moodId = moodId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public Long getMoodId() {
        return moodId;
    }

    public void setMoodId(Long moodId) {
        this.moodId = moodId;
    }
}
