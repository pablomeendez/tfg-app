package com.tfg.tfg_app.rest.dtos;

import java.time.LocalDateTime;
import java.util.List;


public class DiaryEntryResponseDto {

    private Long id;
    private String description;
    private LocalDateTime date;
    private UserDto user;
    private List<ImageDto> images;
    private Long moodId;
    

    public DiaryEntryResponseDto() {
    }

    public DiaryEntryResponseDto(Long id, String description, LocalDateTime date, UserDto user, List<ImageDto> images, Long moodId) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.user = user;
        this.images = images;
        this.moodId = moodId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<ImageDto> getImages() {
        return images;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
    }

    public Long getMoodId() {
        return moodId;
    }

    public void setMoodId(Long moodId) {
        this.moodId = moodId;
    }

}
