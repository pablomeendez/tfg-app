package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserTrophy")
public class UserTrophy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime obtainedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "trophyId", nullable = false)
    private Trophy trophy;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime date;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
