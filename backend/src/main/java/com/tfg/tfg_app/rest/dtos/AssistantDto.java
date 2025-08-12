package com.tfg.tfg_app.rest.dtos;

public class AssistantDto {
    
    private String question;

    public AssistantDto() {
    }

    public AssistantDto(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
