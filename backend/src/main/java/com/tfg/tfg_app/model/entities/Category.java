package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;
import java.util.Set;

import com.tfg.tfg_app.model.common.MapToJsonConverter;

@Entity
@Table(name = "Category")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = MapToJsonConverter.class)
    @Column(length = 1000)
    private Map<String, String> name;
    

    @OneToMany(mappedBy = "category")
    private Set<Habit> habits;

    public Category() {
    }

    public Category(Long id, Map<String, String> name) {
        this.id = id;
        this.name = name; 
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

    public Set<Habit> getHabits() {
        return habits;
    }

    public void setHabits(Set<Habit> habits) {
        this.habits = habits;
    }
}
