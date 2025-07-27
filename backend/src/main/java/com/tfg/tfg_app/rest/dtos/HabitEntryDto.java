package com.tfg.tfg_app.rest.dtos;


public class HabitEntryDto {
    private Long id;
    private UserDto user;
    private UserHabitDto userHabit;
    private String date;
    private int streak;

    public HabitEntryDto(Long id, UserDto user, UserHabitDto userHabit, String date, int streak) {
        this.id = id;
        this.user = user;
        this.userHabit = userHabit;
        this.date = date;
        this.streak = streak;
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

    public UserHabitDto getUserHabit() {
        return userHabit;
    }

    public void setUserHabit(UserHabitDto userHabit) {
        this.userHabit = userHabit;
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
