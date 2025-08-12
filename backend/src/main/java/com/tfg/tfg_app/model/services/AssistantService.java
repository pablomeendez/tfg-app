package com.tfg.tfg_app.model.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.response.ChatResponse;

public interface AssistantService {

    ChatResponse chat(Long userId, String question) throws InstanceNotFoundException, JsonProcessingException;

    List<ChatMessage> getChatResponses(Long userId);
}