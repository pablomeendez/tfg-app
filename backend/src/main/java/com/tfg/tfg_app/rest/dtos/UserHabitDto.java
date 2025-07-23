package com.tfg.tfg_app.rest.dtos;

public class UserHabitDto {

    private Long id;
    private Long userId;
    private Long habitId;

    public UserHabitDto (Long id, Long userId, Long habitId) {
        this.id = id;
        this.userId = userId;
        this.habitId = habitId;

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

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }
}
