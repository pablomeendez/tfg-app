package com.tfg.tfg_app.model.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.DiaryEntryDao;
import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitDao;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.HabitEntryDao;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.entities.UserHabitDao;
import com.tfg.tfg_app.model.entities.Users;

@Service
public class HabitServiceImpl implements HabitService {

    @Autowired
    private HabitDao habitDao;  

    @Autowired
    private HabitEntryDao habitEntryDao;

    @Autowired
    private DiaryEntryDao diaryEntryDao;

    @Autowired
    private UserHabitDao userHabitDao;

    @Autowired
    private UserService userService;
    
    public List<Habit> getAllHabits() {
        return habitDao.findAll();
    }

    public UserHabit createUserHabit(Long userId, Long habitId) throws InstanceNotFoundException {

        Users user = userService.loginFromId(userId);

        Optional<Habit> optHabit = habitDao.findById(habitId);

        if (!optHabit.isPresent()) {
            throw new InstanceNotFoundException("Habit with ID " + habitId + " not found.", Habit.class);
        }

        Habit habit = optHabit.get();

        UserHabit userHabit = new UserHabit();
        userHabit.setUser(user);
        userHabit.setHabit(habit);

        return userHabitDao.save(userHabit);
    }

    public void deleteUserHabit(Long userHabitId) throws InstanceNotFoundException {
        Optional<UserHabit> optUserHabit = userHabitDao.findById(userHabitId);

        if (!optUserHabit.isPresent()) {
            throw new InstanceNotFoundException("UserHabit with ID " + userHabitId + " not found.", UserHabit.class);
        }

        userHabitDao.delete(optUserHabit.get());
    }

    public List<UserHabit> getHabitsByUserId(Long userId) throws InstanceNotFoundException {

        Users user = userService.loginFromId(userId);

        return userHabitDao.findByUserId(userId);
    }

    public HabitEntry createHabitEntry(Long userId, Long userHabitIdLong, Long diaryEntryId) throws InstanceNotFoundException {
        Users user = userService.loginFromId(userId);
        Optional<UserHabit> optUserHabit = userHabitDao.findById(userHabitIdLong);

        if (!optUserHabit.isPresent()) {
            throw new InstanceNotFoundException("UserHabit with ID " + userHabitIdLong + " not found.", UserHabit.class);
        }

        UserHabit userHabit = optUserHabit.get();

        Optional<DiaryEntry> optDiaryEntry = diaryEntryDao.findById(diaryEntryId);

        if (!optDiaryEntry.isPresent()) {
            throw new InstanceNotFoundException("DiaryEntry with ID " + diaryEntryId + " not found.", DiaryEntry.class);
        }

        DiaryEntry diaryEntry = optDiaryEntry.get();

        HabitEntry habitEntry = new HabitEntry();

        habitEntry.setUser(user);
        habitEntry.setUserHabit(userHabit);
        habitEntry.setDate(LocalDateTime.now());
        habitEntry.setDiaryEntry(diaryEntry);

        HabitEntry lastHabitEntry = habitEntryDao.findTopByUserIdOrderByIdDesc(userId);
        
        // Check if the last habit entry was made yesterday to update streak
        if (lastHabitEntry != null && lastHabitEntry.getDate().toLocalDate().equals(LocalDate.now().minusDays(1))) {
            habitEntry.setStreak(lastHabitEntry.getStreak()+1);
        } else {
            habitEntry.setStreak(1);
        }

        return habitEntryDao.save(habitEntry);
    }

    public HabitEntry deleteHabitEntry(Long userId, Long habitEntryId) throws InstanceNotFoundException {
        Optional<HabitEntry> optHabitEntry = habitEntryDao.findById(habitEntryId);

        if (!optHabitEntry.isPresent()) {
            throw new InstanceNotFoundException("HabitEntry with ID " + habitEntryId + " not found.", HabitEntry.class);
        }

        HabitEntry habitEntry = optHabitEntry.get();

        if (!habitEntry.getUser().getId().equals(userId)) {
            throw new InstanceNotFoundException("User does not own this HabitEntry.", Users.class);
        }

        habitEntryDao.delete(habitEntry);
        return habitEntry;
    }
    
    public List<HabitEntry> getHabitEntriesByUserIdAndUserHabitId(Long userId, Long userHabitId) throws InstanceNotFoundException {
        Users user = userService.loginFromId(userId);
        Optional<UserHabit> optUserHabit = userHabitDao.findById(userHabitId);

        if (!optUserHabit.isPresent()) {
            throw new InstanceNotFoundException("UserHabit with ID " + userHabitId + " not found.", UserHabit.class);
        }

        UserHabit userHabit = optUserHabit.get();

        return habitEntryDao.findByUserIdAndUserHabitId(user.getId(), userHabit.getId());
    }

    public HabitEntry getUserBiggestStreak(Long userId) throws InstanceNotFoundException {
        Users user = userService.loginFromId(userId);
        return habitEntryDao.findFirstByUserIdOrderByStreakDesc(user.getId());
    }

    public List<HabitEntry> getUserHabitsAfterDate(Long userId, LocalDateTime date) throws InstanceNotFoundException {
        Users user = userService.loginFromId(userId);
        return habitEntryDao.findByUserIdAndDateBetween(user.getId(), date.minusDays(7), date);
    }

}
