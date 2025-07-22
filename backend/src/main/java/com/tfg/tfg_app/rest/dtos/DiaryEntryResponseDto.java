package com.tfg.tfg_app.rest.dtos;

import java.time.LocalDateTime;

public class DiaryEntryResponseDto {

    private Long id;
    private String description;
    private LocalDateTime date;
    private Long userId;
    private Long moodId;

    public DiaryEntryResponseDto() {
    }

    public DiaryEntryResponseDto(Long id, String description, LocalDateTime date, Long userId, Long moodId) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }

    public Long getMooId() {
        return moodId;
    }

    public void setMoodId(Long moodId) {
        this.moodId = moodId;
    }

}
