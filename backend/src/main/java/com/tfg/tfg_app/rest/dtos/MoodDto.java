package com.tfg.tfg_app.rest.dtos;

public class MoodDto {
    private Long id;
    private String nameEn;
    private String nameEs;
    private String nameGl;
    private String image;

    public MoodDto() {
    }

    public MoodDto(Long id, String nameEn, String nameEs, String nameGl, String image) {
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

    
}
