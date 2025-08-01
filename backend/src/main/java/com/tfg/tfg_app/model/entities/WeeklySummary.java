package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "WeeklySummary")
public class WeeklySummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int habitsCompleted;

    @NotNull
    private int totalEntries;

    @NotNull
    private int trophiesEarned;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "biggestStreak", nullable = false)
    private HabitEntry biggestStreak;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "moodTrendId", nullable = false)
    private Mood moodTrend;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime date;

    public WeeklySummary() {
    
    }

    public WeeklySummary(int habitsCompleted, int totalEntries, int trophiesEarned, HabitEntry biggestStreak, Users user, Mood moodTrend, LocalDateTime date) {
        this.habitsCompleted = habitsCompleted;
        this.totalEntries = totalEntries;
        this.trophiesEarned = trophiesEarned;
        this.biggestStreak = biggestStreak;
        this.user = user;
        this.moodTrend = moodTrend;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public HabitEntry getBiggestStreak() {
        return biggestStreak;
    }

    public void setBiggestStreak(HabitEntry biggestStreak) {
        this.biggestStreak = biggestStreak;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Mood getMoodTrend() {
        return moodTrend;
    }

    public void setMoodTrend(Mood moodTrend) {
        this.moodTrend = moodTrend;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
