package com.tfg.tfg_app.model.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.entities.WeeklySummary;

import dev.langchain4j.model.chat.response.ChatResponse;

@Service
public class AssistantServiceImpl implements AssistantService {

    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @Autowired
    private HabitService habitService;

    @Autowired
    private Assistant assistant;


    @Override
    public ChatResponse chat(Long userId, String question) throws InstanceNotFoundException {

        
        String enrichedMessage;
        try {
            List<WeeklySummary> weeklyData = weeklySummaryService.getWeeklySummariesByUserId(userId, 0, 1).getContent();
            List<UserHabit> userHabits = habitService.getHabitsByUserId(userId);
            
            StringBuilder messageBuilder = new StringBuilder(question);
            messageBuilder.append("\n\n[");
            
            if (!userHabits.isEmpty()) {
                messageBuilder.append("User Habits: ");
                for (int i = 0; i < userHabits.size(); i++) {
                    UserHabit habit = userHabits.get(i);
                    messageBuilder.append(habit.getHabit().getName().get("en"));
                    if (i < userHabits.size() - 1) {
                        messageBuilder.append(", ");
                    }
                }
                messageBuilder.append("; ");
            }
 
            if (!weeklyData.isEmpty()) {
                WeeklySummary summary = weeklyData.get(0);
                messageBuilder.append("Habits Completed: ").append(summary.getHabitsCompleted())
                           .append(", Total Entries: ").append(summary.getTotalEntries())
                           .append(", Trophies Earned: ").append(summary.getTrophiesEarned())
                           .append(", Biggest Streak: ").append(summary.getBiggestStreak())
                           .append(", MoodTrend: ").append(summary.getMoodTrend());
            } else {
                if (userHabits.isEmpty()) {
                    messageBuilder.append("Usuario completamente nuevo sin hábitos ni historial");
                } else {
                    messageBuilder.append("Usuario nuevo con hábitos configurados pero sin historial de resúmenes");
                }
            }
            
            messageBuilder.append("]");
            enrichedMessage = messageBuilder.toString();
            
        } catch (Exception e) {
            System.err.println("Error building context: " + e.getMessage());
            StringBuilder fallbackBuilder = new StringBuilder(question);
            fallbackBuilder.append("\n\n[Usuario sin historial disponible]");
            enrichedMessage = fallbackBuilder.toString();
        }

        return assistant.chat(userId, enrichedMessage);
    }

}
