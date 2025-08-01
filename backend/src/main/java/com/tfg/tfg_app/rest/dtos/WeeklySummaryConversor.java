package com.tfg.tfg_app.rest.dtos;

import java.util.List;

import com.tfg.tfg_app.model.entities.WeeklySummary;

public class WeeklySummaryConversor {

    public static WeeklySummaryDto toWeeklySummaryDto(WeeklySummary weeklySummary) {
        return new WeeklySummaryDto(weeklySummary.getId(), UserConversor.toUserDto(weeklySummary.getUser()), weeklySummary.getDate(), weeklySummary.getHabitsCompleted(), weeklySummary.getTotalEntries(), weeklySummary.getTrophiesEarned(), HabitConversor.toHabitEntryDto(weeklySummary.getBiggestStreak()), DiaryEntryConversor.toMoodDto(weeklySummary.getMoodTrend()));
    }

    public static List<WeeklySummaryDto> toWeeklySummaryDtos(List<WeeklySummary> weeklySummaries) {
        return weeklySummaries.stream()
                .map(WeeklySummaryConversor::toWeeklySummaryDto)
                .toList();
    }
}
