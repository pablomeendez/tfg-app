package com.tfg.tfg_app.rest.dtos;

import java.util.Map;

public class HabitDto {
    
    private Long id;
    private Map<String, String> name;
    private Map<String, String> description;
    private CategoryDto category;

    public HabitDto(Long id, Map<String, String> name, Map<String, String> description, CategoryDto category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
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

    public CategoryDto getCategory() {
        return category;
    }

}
