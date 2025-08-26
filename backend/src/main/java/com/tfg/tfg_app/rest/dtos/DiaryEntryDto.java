package com.tfg.tfg_app.rest.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class DiaryEntryDto {
    
    private String description;
    private List<byte[]> images;
    private LocalDateTime date;
    private List<UserHabitDto> habits; 
    private MoodDto mood;
 
    public DiaryEntryDto() {
    }

    public DiaryEntryDto(String description, List<byte[]> images, LocalDateTime date, List<UserHabitDto> habits, MoodDto mood) {
        this.description = description;
        this.images = images;
        this.date = date;
        this.habits = habits;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<UserHabitDto> getHabits() {
        return habits;
    }

    public void setHabits(List<UserHabitDto> habits) {
        this.habits = habits;
    }

    public MoodDto getMood() {
        return mood;
    }

    public void setMood(MoodDto mood) {
        this.mood = mood;
    }
}
