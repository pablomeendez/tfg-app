package com.tfg.tfg_app.rest.dtos;

import java.time.LocalDateTime;


public class WeeklySummaryDto {

    private Long id;
    private UserDto user;
    private LocalDateTime date;
    private int habitsCompleted;
    private int totalEntries;
    private int trophiesEarned;
    private int biggestStreak;
    private MoodDto moodTrend;

    public WeeklySummaryDto() {
    }

    public WeeklySummaryDto(Long id, UserDto user, LocalDateTime date, int habitsCompleted, int totalEntries, int trophiesEarned, int biggestStreak, MoodDto moodTrend) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.habitsCompleted = habitsCompleted;
        this.totalEntries = totalEntries;
        this.trophiesEarned = trophiesEarned;
        this.biggestStreak = biggestStreak;
        this.moodTrend = moodTrend;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getHabitsCompleted() {
        return habitsCompleted;
    }

    public void setHabitsCompleted(int habitsCompleted) {
        this.habitsCompleted = habitsCompleted;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }

    public int getTrophiesEarned() {
        return trophiesEarned;
    }

    public void setTrophiesEarned(int trophiesEarned) {
        this.trophiesEarned = trophiesEarned;
    }

    public int getBiggestStreak() {
        return biggestStreak;
    }

    public void setBiggestStreak(int biggestStreak) {
        this.biggestStreak = biggestStreak;
    }

    public MoodDto getMoodTrend() {
        return moodTrend;
    }

    public void setMoodTrend(MoodDto moodTrend) {
        this.moodTrend = moodTrend;
    }
}
