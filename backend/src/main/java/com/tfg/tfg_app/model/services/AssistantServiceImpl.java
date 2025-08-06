package com.tfg.tfg_app.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssistantServiceImpl implements AssistantService {
    
    @Autowired
    private Assistant assistant;

    @Override
    public String chat(String question) {
        return assistant.chat(question);
    }

}
