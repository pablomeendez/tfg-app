package com.tfg.tfg_app.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.WeeklySummary;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

@Service
public class AssistantServiceImpl implements AssistantService {

    @Value("${OPENAI_API_KEY}")
    private String openAiApiKey;

    @Value("${OPENAI_MODEL}")
    private String modelName;

    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @Override
    public ChatResponse chat(Long userId, String question) throws InstanceNotFoundException, JsonProcessingException{
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(modelName)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(20))
                .build();

        WeeklySummary weeklySummary = weeklySummaryService.getWeeklySummariesByUserId(userId, 0, 1).getContent().get(0);

        ObjectMapper objectMapper = new ObjectMapper();

        UserMessage userMessage = UserMessage.from(objectMapper.writeValueAsString(weeklySummary), question);

        return assistant.chat(userId, userMessage.toString());
    }

}
