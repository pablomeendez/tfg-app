package com.tfg.tfg_app.model.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.entities.WeeklySummary;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;

@Service
public class AssistantServiceImpl implements AssistantService {

    private final Assistant assistant;

    private final InMemoryChatMemoryStore chatMemoryStore;

    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @Autowired
    private HabitService habitService;

    //Singleton
    public AssistantServiceImpl(@Value("${OPENAI_API_KEY}") String openAiApiKey, @Value("${OPENAI_MODEL}") String modelName) {
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(modelName)
                .temperature(0.7)
                .build();

        this.chatMemoryStore = new InMemoryChatMemoryStore();
        

        this.assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .chatMemoryStore(this.chatMemoryStore)
                        .maxMessages(20)
                        .build())
                .build();
    }

    @Override
    public ChatResponse chat(Long userId, String question) throws InstanceNotFoundException{

        List<WeeklySummary> weeklySummaryList = weeklySummaryService.getWeeklySummariesByUserId(userId, 0, 1).getContent();

        WeeklySummary weeklySummary = null;

        if (!weeklySummaryList.isEmpty()) {
            weeklySummary = weeklySummaryList.get(0);
        }

        List<UserHabit> userHabits = habitService.getHabitsByUserId(userId);

        String weeklySummaryData = weeklySummary != null ? 
            String.format("Weekly Summary - Habits completed: %d, Total entries: %d, Trophies earned: %d", 
                weeklySummary.getHabitsCompleted(), weeklySummary.getTotalEntries(), weeklySummary.getTrophiesEarned()) : 
            "No weekly summary available";

        String userHabitsData = userHabits.stream()
            .map(uh -> String.format("Habit: %s", uh.getHabit().getName()))
            .reduce("", (acc, habit) -> acc + (acc.isEmpty() ? "" : ", ") + habit);

        String combinedMessage = "Weekly Summary: " + weeklySummaryData
                + "\nUser Habits: " + userHabitsData
                + "\nQuestion: " + question;

        return assistant.chat(userId, combinedMessage);
    }

    @Override
    public List<ChatMessage> getChatResponses(Long userId) {
        return chatMemoryStore.getMessages(userId);
    }

}
