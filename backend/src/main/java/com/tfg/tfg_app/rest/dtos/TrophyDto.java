package com.tfg.tfg_app.rest.dtos;

import java.util.Map;

public class TrophyDto {
    private Long id;
    private Map<String, String> name;
    private Map<String, String> description;
    private int days;
    private String image;

    public TrophyDto() {
    }

    public TrophyDto(Long id, Map<String, String> name, Map<String, String> description, int days, String image) {
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
}
