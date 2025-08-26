package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserTrophy")
public class UserTrophy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime obtainedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trophyId", nullable = false)
    private Trophy trophy;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitId", nullable = false)
    private Habit habit;

    public UserTrophy() {
    }

    public UserTrophy(Users user, Trophy trophy, Habit habit, LocalDateTime obtainedAt) {
        this.user = user;
        this.trophy = trophy;
        this.habit = habit;
        this.obtainedAt = obtainedAt;
    }

    public UserTrophy( Long id, Users user, Trophy trophy, Habit habit, LocalDateTime obtainedAt) {
        this.id = id;
        this.obtainedAt = obtainedAt;
        this.user = user;
        this.trophy = trophy;
        this.habit = habit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getObtainedAt() {
        return obtainedAt;
    }

    public void setObtainedAt(LocalDateTime obtainedAt) {
        this.obtainedAt = obtainedAt;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Trophy getTrophy() {
        return trophy;
    }

    public void setTrophy(Trophy trophy) {
        this.trophy = trophy;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }
}
