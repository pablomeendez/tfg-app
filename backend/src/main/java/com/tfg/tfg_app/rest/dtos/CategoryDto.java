package com.tfg.tfg_app.rest.dtos;

import java.util.Map;

public class CategoryDto {
    private Long id;
    private Map<String, String> name;

    public CategoryDto() {
    }

    public CategoryDto(Long id, Map<String, String> name) {
        this.id = id;
        this.name = name;
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

}