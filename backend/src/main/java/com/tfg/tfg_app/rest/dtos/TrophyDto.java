package com.tfg.tfg_app.rest.dtos;

public class TrophyDto {
    private Long id;
    private String name;
    private String description;
    private int days;
    private String image;

    public TrophyDto() {
    }

    public TrophyDto(Long id, String name, String description, int days, String image) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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
