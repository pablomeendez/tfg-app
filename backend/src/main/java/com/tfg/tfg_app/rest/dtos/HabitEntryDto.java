package com.tfg.tfg_app.rest.dtos;


public class HabitEntryDto {
    private Long id;
    private UserDto user;
    private HabitDto habit;
    private DiaryEntryDto diaryEntry;
    private String date;
    private UserTrophyDto userTrophy;
    private int streak;

    public HabitEntryDto(Long id, UserDto user, HabitDto habit, DiaryEntryDto diaryEntry, String date, UserTrophyDto userTrophy, int streak) {
        this.id = id;
        this.user = user;
        this.habit = habit;
        this.diaryEntry = diaryEntry;
        this.date = date;
        this.userTrophy = userTrophy;
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

    public HabitDto getHabit() {
        return habit;
    }

    public void setHabit(HabitDto habit) {
        this.habit = habit;
    }

    public DiaryEntryDto getDiaryEntry() {
        return diaryEntry;
    }

    public void setDiaryEntry(DiaryEntryDto diaryEntry) {
        this.diaryEntry = diaryEntry;
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

    public UserTrophyDto getUserTrophy() {
        return userTrophy;
    }

    public void setUserTrophy(UserTrophyDto userTrophy) {
        this.userTrophy = userTrophy;
    }
}
