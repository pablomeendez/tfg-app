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

    public Trophy(String nameEn, String nameEs, String nameGl, String descriptionEn, String descriptionEs, String descriptionGl, int days, String image) {
        this.nameEn = nameEn;
        this.nameEs = nameEs;
        this.nameGl = nameGl;
        this.descriptionEn = descriptionEn;
        this.descriptionEs = descriptionEs;
        this.descriptionGl = descriptionGl;
        this.days = days;
        this.image = image;
    }

    public Trophy(Long id, String nameEn, String nameEs, String nameGl, String descriptionEn, String descriptionEs, String descriptionGl, int days, String image) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameEs = nameEs;
        this.nameGl = nameGl;
        this.descriptionEn = descriptionEn;
        this.descriptionEs = descriptionEs;
        this.descriptionGl = descriptionGl;
        this.days = days;
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
