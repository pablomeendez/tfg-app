package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "HabitEntry")
public class HabitEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "userHabitId", nullable = false)
    private UserHabit userHabit;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime date;

    @NotNull
    @Column(nullable = false)
    private int streak;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "diaryEntryId", nullable = false)    
    private DiaryEntry diaryEntry;


    public HabitEntry() {
    }   

    public HabitEntry(Users user, UserHabit userHabit, DiaryEntry diaryEntry, LocalDateTime date, int streak) {
        this.user = user;
        this.userHabit = userHabit;
        this.diaryEntry = diaryEntry;
        this.date = date;
        this.streak = streak;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserHabit getUserHabit() {
        return userHabit;
    }

    public void setUserHabit(UserHabit userHabit) {
        this.userHabit = userHabit;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public DiaryEntry getDiaryEntry() {
        return diaryEntry;
    }

    public void setDiaryEntry(DiaryEntry diaryEntry) {
        this.diaryEntry = diaryEntry;
    }
}
