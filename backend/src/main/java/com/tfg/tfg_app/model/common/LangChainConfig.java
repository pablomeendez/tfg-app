package com.tfg.tfg_app.model.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tfg.tfg_app.model.services.Assistant;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;

@Configuration
public class LangChainConfig {

    @Value("${OPENAI_API_KEY}")
    private String openAiApiKey;

    @Value("${OPENAI_MODEL}")
    private String modelName;

    @Bean
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(modelName)
                .temperature(0.7)
                .maxTokens(1000)
                .build();
    }

    @Bean
    public Assistant assistant(ChatModel chatModel) {
        InMemoryChatMemoryStore store = new InMemoryChatMemoryStore();
        
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(store)
                .build();
                
        return AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
