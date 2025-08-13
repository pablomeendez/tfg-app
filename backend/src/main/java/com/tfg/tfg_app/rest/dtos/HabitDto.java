package com.tfg.tfg_app.rest.dtos;

public class HabitDto {
    
    private Long id;
    private String nameEn;
    private String nameEs;
    private String nameGl;
    private String descriptionEn;
    private String descriptionEs;
    private String descriptionGl;
    private CategoryDto category;
    private String imageString;

    public HabitDto(Long id, String nameEn, String nameEs, String nameGl, String descriptionEn, String descriptionEs, String descriptionGl, CategoryDto category, String imageString) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameEs = nameEs;
        this.nameGl = nameGl;
        this.descriptionEn = descriptionEn;
        this.descriptionEs = descriptionEs;
        this.descriptionGl = descriptionGl;
        this.category = category;
        this.imageString = imageString;
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

    public CategoryDto getCategory() {
        return category;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
