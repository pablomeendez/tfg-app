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
    private String name;

    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "mood")
    private Set<DiaryEntry> diaryEntries;

    @OneToMany(mappedBy = "moodTrend")
    private Set<WeeklySummary> weeklySummaries;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Set<DiaryEntry> getDiaryEntries() {
        return diaryEntries;
    }

    public void setDiaryEntries(Set<DiaryEntry> diaryEntries) {
        this.diaryEntries = diaryEntries;
    }

    public Set<WeeklySummary> getWeeklySummaries() {
        return weeklySummaries;
    }

    public void setWeeklySummaries(Set<WeeklySummary> weeklySummaries) {
        this.weeklySummaries = weeklySummaries;
    }
}
