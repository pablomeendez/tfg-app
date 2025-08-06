package com.tfg.tfg_app.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.tfg_app.model.services.AssistantService;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {
    
    @Autowired
    private AssistantService assistantService;

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String question) {
        try {
            String response = assistantService.chat(question);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error en chat assistant: " + e.getMessage());
            return ResponseEntity.ok("Lo siento, el asistente de IA no está disponible en este momento. Por favor, inténtalo más tarde.");
        }
    }

}
