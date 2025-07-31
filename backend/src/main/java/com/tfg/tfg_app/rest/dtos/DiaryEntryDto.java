package com.tfg.tfg_app.rest.dtos;

import java.util.List;

public class DiaryEntryDto {
    
    private String description;
    private List<byte[]> images;
    private MoodDto mood;

    public DiaryEntryDto() {
    }

    public DiaryEntryDto(String description, List<byte[]> images, MoodDto mood) {
        this.description = description;
        this.images = images;
        this.mood = mood;
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

    public MoodDto getMood() {
        return mood;
    }

    public void setMood(MoodDto mood) {
        this.mood = mood;
    }
}
