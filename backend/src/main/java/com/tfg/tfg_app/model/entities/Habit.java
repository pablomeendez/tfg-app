package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Set;

import com.tfg.tfg_app.model.common.MapToJsonConverter;

@Entity
@Table(name = "Habit")
public class Habit {
    
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
    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "habit")
    private Set<UserHabit> userHabits;

    public Habit() {
    }

    public Habit(Long id, Map<String, String> name, Map<String, String> description, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<UserHabit> getUserHabits() {
        return userHabits;
    }

    public void setUserHabits(Set<UserHabit> userHabits) {
        this.userHabits = userHabits;
    }
}
