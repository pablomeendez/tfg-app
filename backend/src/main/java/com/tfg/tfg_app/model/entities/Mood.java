package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "Mood")
public class Mood {
    
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

    @Lob
    private String image;

    @OneToMany(mappedBy = "mood")
    private Set<DiaryEntry> diaryEntries;

    public Mood() {
    }

    public Mood(Long id, String nameEn, String nameEs, String nameGl, String image) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameEs = nameEs;
        this.nameGl = nameGl;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<DiaryEntry> getDiaryEntries() {
        return diaryEntries;
    }

    public void setDiaryEntries(Set<DiaryEntry> diaryEntries) {
        this.diaryEntries = diaryEntries;
    }
}
