package com.tfg.tfg_app.rest.dtos;

import java.util.Map;

public class MoodDto {
    private Long id;
    private Map<String, String> name; 
    private String image;

    public MoodDto() {
    }

    public MoodDto(Long id, Map<String, String> name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
    

    public Long getId() {
        return id;
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

    
}
