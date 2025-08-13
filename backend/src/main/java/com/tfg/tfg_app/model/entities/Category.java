package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "Category")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nameEn;

    @NotBlank
    @Column(nullable = false)
    private String nameEs;

    @NotBlank
    @Column(nullable = false)
    private String nameGl;

    @OneToMany(mappedBy = "category")
    private Set<Habit> habits;

    public Category() {
    }

    public Category(Long id, String nameEn, String nameEs, String nameGl) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameEs = nameEs;
        this.nameGl = nameGl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameEs() {
        return nameEs;
    }

    public void setNameEs(String nameEs) {
        this.nameEs = nameEs;
    }

    public String getNameGl() {
        return nameGl;
    }

    public void setNameGl(String nameGl) {
        this.nameGl = nameGl;
    }

    public Set<Habit> getHabits() {
        return habits;
    }

    public void setHabits(Set<Habit> habits) {
        this.habits = habits;
    }
}
