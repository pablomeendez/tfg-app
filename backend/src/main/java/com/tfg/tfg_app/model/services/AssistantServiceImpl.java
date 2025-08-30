package com.tfg.tfg_app.model.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.WeeklySummary;

import dev.langchain4j.model.chat.response.ChatResponse;

@Service
public class AssistantServiceImpl implements AssistantService {

    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @Autowired
    private Assistant assistant;


    @Override
    public ChatResponse chat(Long userId, String question) throws InstanceNotFoundException, JsonProcessingException {
        
        String enrichedMessage;
        try {
            List<WeeklySummary> weeklyData = weeklySummaryService.getWeeklySummariesByUserId(userId, 0, 1).getContent();
            
            if (!weeklyData.isEmpty()) {
                enrichedMessage = String.format(
                    "%s\n\n[UserContext: Habits Completed: %s, Total Entries: %s, Trophies Earned: %s, Biggest Streak %s, MoodTrend: %s], ",
                    question,
                    weeklyData.get(0).getHabitsCompleted(),
                    weeklyData.get(0).getTotalEntries(),
                    weeklyData.get(0).getTrophiesEarned(),
                    weeklyData.get(0).getBiggestStreak(),
                    weeklyData.get(0).getMoodTrend()
                );
            } else {
                enrichedMessage = question + "\n\n[El usuario es nuevo, no hay historial disponible]";
            }
        } catch (Exception e) {
            System.err.println("Error building context: " + e.getMessage());
            enrichedMessage = question + "\n\n[Usuario sin historial disponible]";
        }

        return assistant.chat(userId, enrichedMessage);
    }

}
