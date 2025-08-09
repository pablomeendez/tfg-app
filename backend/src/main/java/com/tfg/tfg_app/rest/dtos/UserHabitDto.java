package com.tfg.tfg_app.rest.dtos;

public class UserHabitDto {

    private Long id;
    private UserDto user;
    private HabitDto habit;

    public UserHabitDto (Long id, UserDto user, HabitDto habit) {
        this.id = id;
        this.user = user;
        this.habit = habit;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public HabitDto getHabit() {
        return habit;
    }

    public void setHabit(HabitDto habit) {
        this.habit = habit;
    }
}
