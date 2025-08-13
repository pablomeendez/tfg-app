package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "Habit")
public class Habit {
    
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

    @NotBlank
    @Column(nullable = false)
    private String descriptionEn;

    @NotBlank
    @Column(nullable = false)
    private String descriptionEs;

    @NotBlank
    @Column(nullable = false)
    private String descriptionGl;

    @NotNull
    @Lob
    @Column(nullable = false)
    private String image;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "habit")
    private Set<UserHabit> userHabits;

    public Habit() {
    }

    public Habit(Long id, String nameEn, String nameEs, String nameGl, String descriptionEn, String descriptionEs, String descriptionGl, Category category, String image) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameEs = nameEs;
        this.nameGl = nameGl;
        this.descriptionEn = descriptionEn;
        this.descriptionEs = descriptionEs;
        this.descriptionGl = descriptionGl;
        this.category = category;
        this.image = image;
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

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionEs() {
        return descriptionEs;
    }

    public void setDescriptionEs(String descriptionEs) {
        this.descriptionEs = descriptionEs;
    }

    public String getDescriptionGl() {
        return descriptionGl;
    }

    public void setDescriptionGl(String descriptionGl) {
        this.descriptionGl = descriptionGl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
