package com.tfg.tfg_app.rest.dtos;

import java.time.LocalDateTime;

public class UserTrophyDto {

    private Long id;
    private UserDto user;
    private TrophyDto trophy;
    private HabitDto habit;
    private LocalDateTime obtainedAt;

    public UserTrophyDto() {
    }

    public UserTrophyDto(Long id, UserDto user, TrophyDto trophy, HabitDto habit, LocalDateTime obtainedAt) {
        this.id = id;
        this.user = user;
        this.trophy = trophy;
        this.habit = habit;
        this.obtainedAt = obtainedAt;
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

    public TrophyDto getTrophy() {
        return trophy;
    }

    public void setTrophy(TrophyDto trophy) {
        this.trophy = trophy;
    }

    public HabitDto getHabit() {
        return habit;
    }

    public void setHabit(HabitDto habit) {
        this.habit = habit;
    }

    public LocalDateTime getObtainedAt() {
        return obtainedAt;
    }

    public void setObtainedAt(LocalDateTime obtainedAt) {
        this.obtainedAt = obtainedAt;
    }

}
