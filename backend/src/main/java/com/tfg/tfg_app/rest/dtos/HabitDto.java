package com.tfg.tfg_app.rest.dtos;

public class HabitDto {
    
    private Long id;
    private String name;
    private String description;
    private String imageString;

    public HabitDto(Long id, String name, String description, String imageString) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageString = imageString;
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

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
