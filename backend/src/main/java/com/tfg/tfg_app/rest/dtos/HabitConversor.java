package com.tfg.tfg_app.rest.dtos;

import java.util.List;

import com.tfg.tfg_app.model.entities.Category;
import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.entities.UserTrophy;

public class HabitConversor {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getNameEn(), category.getNameEs(), category.getNameGl());
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getNameEn(), categoryDto.getNameEs(), categoryDto.getNameGl());
    }
    
    public static HabitDto toHabitDto (Habit habit) {
        return new HabitDto(habit.getId(), habit.getNameEn(), habit.getNameEs(), habit.getNameGl(), habit.getDescriptionEn(), habit.getDescriptionEs(), habit.getDescriptionGl(), toCategoryDto(habit.getCategory()), habit.getImage().toString());
    }

    public static Habit toHabit(HabitDto habitDto) {
        return new Habit(habitDto.getId(), habitDto.getNameEn(), habitDto.getNameEs(), habitDto.getNameGl(), habitDto.getDescriptionEn(), habitDto.getDescriptionEs(), habitDto.getDescriptionGl(), toCategory(habitDto.getCategory()), habitDto.getImageString());
    }

    public static List<HabitDto> toHabitDtos(List<Habit> habits) {
        return habits.stream().map(HabitConversor::toHabitDto).toList();
    }

    public static UserHabitDto toUserHabitDto(UserHabit userHabit) {
        return new UserHabitDto(userHabit.getId(), UserConversor.toUserDto(userHabit.getUser()), toHabitDto(userHabit.getHabit()));
    }

    public static List<UserHabitDto> toUserHabitDtos(List<UserHabit> userHabits) {
        return userHabits.
        stream().map(HabitConversor::toUserHabitDto).toList();
    }

    public static UserHabit toUserHabit(UserHabitDto userHabitDto) {
        return new UserHabit(userHabitDto.getId(), UserConversor.toUser(userHabitDto.getUser()), toHabit(userHabitDto.getHabit()));
    }

    public static List<UserHabit> toUserHabits(List<UserHabitDto> userHabitDtos) {
        return userHabitDtos.stream().map(HabitConversor::toUserHabit).toList();
    }

    public static HabitEntryDto toHabitEntryDto(HabitEntry habitEntry) {
        return new HabitEntryDto(habitEntry.getId(), UserConversor.toUserDto(habitEntry.getUser()), toHabitDto(habitEntry.getHabit()), DiaryEntryConversor.toDiaryEntryDto(habitEntry.getDiaryEntry()), habitEntry.getDate().toString(), habitEntry.getStreak());
    }

    public static List<HabitEntryDto> toHabitEntryDtos(List<HabitEntry> habitEntries) {
        return habitEntries.stream().map(HabitConversor::toHabitEntryDto).toList();
    }

    public static HabitEntryWithTrophyDto toHabitEntryWithTrophyDto(HabitEntry habitEntry, UserTrophy userTrophy) {
        HabitEntryDto habitEntryDto = toHabitEntryDto(habitEntry);
        if (userTrophy == null) {
            return new HabitEntryWithTrophyDto(habitEntryDto, null);
        }
        UserTrophyDto userTrophyDto = TrophyConversor.toUserTrophyDto(userTrophy);
        return new HabitEntryWithTrophyDto(habitEntryDto, userTrophyDto);
    }
}
