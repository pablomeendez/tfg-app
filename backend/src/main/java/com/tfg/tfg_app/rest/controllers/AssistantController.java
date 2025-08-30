package com.tfg.tfg_app.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.tfg_app.model.services.AssistantService;
import com.tfg.tfg_app.rest.dtos.AssistantDto;

import dev.langchain4j.model.chat.response.ChatResponse;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {
    
    @Autowired
    private AssistantService assistantService;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestAttribute Long userId, @RequestBody AssistantDto question) {
        try {
            ChatResponse response = assistantService.chat(userId, question.getQuestion());
            return ResponseEntity.ok(response.aiMessage().text());
        } catch (Exception e) {     
            System.err.println("Error en chat assistant: " + e.getMessage());
            return ResponseEntity.badRequest().body("Lo siento, el asistente de IA no está disponible en este momento. Por favor, inténtalo más tarde.");
        }
    }

}
