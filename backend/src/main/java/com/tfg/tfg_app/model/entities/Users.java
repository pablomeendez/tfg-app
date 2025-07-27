package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Users")
public class Users {

    public enum Role {
        USER,
        ADMIN
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String userName;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean firstEntry = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<DiaryEntry> diaryEntries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserHabit> userHabits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<HabitEntry> habitEntries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserTrophy> userTrophies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<WeeklySummary> weeklySummaries;

    public Users() {
    }

    public Users(String userName, String password, String name, String lastName, String email) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public Users(Long id, String userName, String password, String name, String lastName, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public Users(Long id, String userName, String password, String name, String lastName, String email, Boolean firstEntry) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.firstEntry = firstEntry;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getFirstEntry() {
        return firstEntry;
    }

    public void setFirstEntry(Boolean firstEntry) {
        this.firstEntry = firstEntry;
    }

    public Set<DiaryEntry> getDiaryEntries() {
        return diaryEntries;
    }

    public void setDiaryEntries(Set<DiaryEntry> diaryEntries) {
        this.diaryEntries = diaryEntries;
    }

    public Set<UserHabit> getUserHabits() {
        return userHabits;
    }

    public void setUserHabits(Set<UserHabit> userHabits) {
        this.userHabits = userHabits;
    }

    public Set<HabitEntry> getHabitEntries() {
        return habitEntries;
    }

    public void setHabitEntries(Set<HabitEntry> habitEntries) {
        this.habitEntries = habitEntries;
    }

    public Set<UserTrophy> getUserTrophies() {
        return userTrophies;
    }

    public void setUserTrophies(Set<UserTrophy> userTrophies) {
        this.userTrophies = userTrophies;
    }

    public Set<WeeklySummary> getWeeklySummaries() {
        return weeklySummaries;
    }

    public void setWeeklySummaries(Set<WeeklySummary> weeklySummaries) {
        this.weeklySummaries = weeklySummaries;
    }
} 