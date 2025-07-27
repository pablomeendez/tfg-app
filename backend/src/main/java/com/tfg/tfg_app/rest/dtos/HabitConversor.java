package com.tfg.tfg_app.rest.dtos;

import java.util.List;

import com.tfg.tfg_app.model.entities.Category;
import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.UserHabit;

public class HabitConversor {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
    
    public static HabitDto toHabitDto (Habit habit) {
        return new HabitDto(habit.getId(), habit.getName(), habit.getDescription(), toCategoryDto(habit.getCategory()), habit.getImage().toString());
    }

    public static List<HabitDto> toHabitDtos(List<Habit> habits) {
        return habits.stream().map(HabitConversor::toHabitDto).toList();
    }

    public static UserHabitDto toUserHabitDto(UserHabit userHabit) {
        return new UserHabitDto(userHabit.getId(), userHabit.getUser().getId(), toHabitDto(userHabit.getHabit()));
    }

    public static List<UserHabitDto> toUserHabitDtos(List<UserHabit> userHabits) {
        return userHabits.stream().map(HabitConversor::toUserHabitDto).toList();
    }

    public static HabitEntryDto toHabitEntryDto(HabitEntry habitEntry) {
        return new HabitEntryDto(habitEntry.getId(), UserConversor.toUserDto(habitEntry.getUser()), toUserHabitDto(habitEntry.getUserHabit()), habitEntry.getDate().toString(), habitEntry.getStreak());
    }

    public static List<HabitEntryDto> toHabitEntryDtos(List<HabitEntry> habitEntries) {
        return habitEntries.stream().map(HabitConversor::toHabitEntryDto).toList();
    }
}
