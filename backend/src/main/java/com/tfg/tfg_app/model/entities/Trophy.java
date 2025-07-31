package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "Trophy")
public class Trophy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private int days;

    @NotNull
    @Lob
    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "trophy")
    private Set<UserTrophy> userTrophies;

    public Trophy() {
    }

    public Trophy(String name, String description, int days, String image) {
        this.name = name;
        this.description = description;
        this.days = days;
        this.image = image;
    }

    public Trophy(Long id, String name, String description, int days, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.days = days;
        this.image = image;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<UserTrophy> getUserTrophies() {
        return userTrophies;
    }

    public void setUserTrophies(Set<UserTrophy> userTrophies) {
        this.userTrophies = userTrophies;
    }
}
