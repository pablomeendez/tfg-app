package com.tfg.tfg_app.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;
import java.util.Set;

import com.tfg.tfg_app.model.common.MapToJsonConverter;

@Entity
@Table(name = "Mood")
public class Mood {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = MapToJsonConverter.class)
    @Column(length = 1000)
    private Map<String, String> name;

    @Lob
    private String image;

    @OneToMany(mappedBy = "mood")
    private Set<DiaryEntry> diaryEntries;

    public Mood() {
    }

    public Mood(Long id, Map<String, String> name, String image) {
        this.id = id;
        this.name = name;
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
