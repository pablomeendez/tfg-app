package com.tfg.tfg_app.rest.dtos;

public class UserHabitDto {

    private Long id;
    private Long userId;
    private HabitDto habit;

    public UserHabitDto (Long id, Long userId, HabitDto habit) {
        this.id = id;
        this.userId = userId;
        this.habit = habit;

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

    public HabitDto getHabit() {
        return habit;
    }

    public void setHabit(HabitDto habit) {
        this.habit = habit;
    }
}
