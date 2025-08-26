package com.tfg.tfg_app.model.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;

import dev.langchain4j.model.chat.response.ChatResponse;

public interface AssistantService {

    ChatResponse chat(Long userId, String question) throws InstanceNotFoundException, JsonProcessingException;
}