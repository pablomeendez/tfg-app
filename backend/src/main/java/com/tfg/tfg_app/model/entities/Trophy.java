package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Set;

import com.tfg.tfg_app.model.common.MapToJsonConverter;

@Entity
@Table(name = "Trophy")
public class Trophy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = MapToJsonConverter.class)
    @Column(length = 1000)
    private Map<String, String> name;

    @Convert(converter = MapToJsonConverter.class)
    @Column(length = 2000)
    private Map<String, String> description;

    @NotNull
    @Column(nullable = false)
    private int days;

    @NotNull
    @Lob
    @Column(nullable = false)
    private String image;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trophy")
    private Set<UserTrophy> userTrophies;

    public Trophy() {
    }

    public Trophy(Map<String, String> name, Map<String, String> description, int days, String image) {
        this.name = name;
        this.description = description;
        this.days = days;
        this.image = image;
    }

    public Trophy(Long id, Map<String, String> name, Map<String, String> description, int days, String image) {
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

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
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
