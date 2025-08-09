package com.tfg.tfg_app.rest.dtos;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.tfg.tfg_app.model.entities.WeeklySummary;

public class WeeklySummaryConversor {

    public static WeeklySummaryDto toWeeklySummaryDto(WeeklySummary weeklySummary) {
        return new WeeklySummaryDto(weeklySummary.getId(), UserConversor.toUserDto(weeklySummary.getUser()), weeklySummary.getDate(), weeklySummary.getHabitsCompleted(), weeklySummary.getTotalEntries(), weeklySummary.getTrophiesEarned(), HabitConversor.toHabitEntryDto(weeklySummary.getBiggestStreak()), DiaryEntryConversor.toMoodDto(weeklySummary.getMoodTrend()));
    }

    public static Page<WeeklySummaryDto> toWeeklySummaryDtoPage(Page<WeeklySummary> weeklySummaries) {
        return new PageImpl<>(weeklySummaries.stream()
                .map(WeeklySummaryConversor::toWeeklySummaryDto)
                .collect(Collectors.toList()), weeklySummaries.getPageable(), weeklySummaries.getTotalElements());
    }
}
