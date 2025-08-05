package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;
import java.util.List;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.UserHabit;

public interface HabitService {

    //Habit createHabit(Habit habit);

    //Habit updateHabit(Habit habit);

    //void deleteHabit(Habit habit);

    //Habit getHabitById(Long habitId);
    
    List<Habit> getAllHabits();

    UserHabit createUserHabit(Long userId, Long habitId) throws InstanceNotFoundException;

    void deleteUserHabit(Long userHabitId) throws InstanceNotFoundException;

    List<UserHabit> getHabitsByUserId(Long userId) throws InstanceNotFoundException;

    HabitEntry createHabitEntry(Long userId, Long userHabitId, Long diaryEntryId) throws InstanceNotFoundException;

    HabitEntry deleteHabitEntry(Long userId, Long habitEntryId) throws InstanceNotFoundException;

    List<HabitEntry> getHabitEntriesByUserIdAndHabitId(Long userId, Long habitId) throws InstanceNotFoundException;

    HabitEntry getUserBiggestStreak(Long userId) throws InstanceNotFoundException;

    List<HabitEntry> getUserHabitsAfterDate(Long userId, LocalDateTime date) throws InstanceNotFoundException;
    
}
