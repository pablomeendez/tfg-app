package com.tfg.tfg_app.rest.dtos;

public class HabitEntryDto {
    private Long id;
    private Long userId;
    private Long userHabitId;
    private String date;
    private int streak;

    public HabitEntryDto(Long id, Long userId, Long userHabitId, String date, int streak) {
        this.id = id;
        this.userId = userId;
        this.userHabitId = userHabitId;
        this.date = date;
        this.streak = streak;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserHabitId() {
        return userHabitId;
    }

    public void setUserHabitId(Long userHabitId) {
        this.userHabitId = userHabitId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }
}
