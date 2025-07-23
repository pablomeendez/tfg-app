package com.tfg.tfg_app.rest.dtos;

import java.util.List;

import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.UserHabit;

public class HabitConversor {
    
    public static HabitDto toHabitDto (Habit habit) {
        return new HabitDto(habit.getId(), habit.getName(), habit.getDescription(), habit.getImage().toString());
    }

    public static List<HabitDto> toHabitDtos(List<Habit> habits) {
        return habits.stream().map(HabitConversor::toHabitDto).toList();
    }

    public static UserHabitDto toUserHabitDto(UserHabit userHabit) {
        return new UserHabitDto(userHabit.getId(), userHabit.getUser().getId(), userHabit.getHabit().getId());
    }

    public static List<UserHabitDto> toUserHabitDtos(List<UserHabit> userHabits) {
        return userHabits.stream().map(HabitConversor::toUserHabitDto).toList();
    }

    public static HabitEntryDto toHabitEntryDto(HabitEntry habitEntry) {
        return new HabitEntryDto(habitEntry.getId(), habitEntry.getUser().getId(), habitEntry.getUserHabit().getId(), habitEntry.getDate().toString(), habitEntry.getStreak());
    }
}
