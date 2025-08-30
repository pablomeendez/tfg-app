package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.entities.UsersDao;
import com.tfg.tfg_app.model.entities.WeeklySummary;
import com.tfg.tfg_app.model.entities.WeeklySummaryDao;

@Service
public class WeeklySummaryServiceImpl implements WeeklySummaryService {

    @Autowired
    private WeeklySummaryDao weeklySummaryDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private UserService userService;
    
    @Autowired
    private DiaryEntryService diaryEntryService;

    @Autowired
    private HabitService habitService;

    @Autowired
    private TrophyService trophyService;

    @Scheduled(cron = "59 * * * * *")
    public void generateWeeklySummariesForAllUsers() {
        try {
            List<Users> allUsers = usersDao.findAll();
            LocalDateTime now = LocalDateTime.now();
            
            for (Users user : allUsers) {
                try {
                    generateWeeklySummary(user.getId(), now);
                } catch (Exception e) {
                    System.err.println("Error generating weekly summary for user " + user.getId() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error in scheduled weekly summary task: " + e.getMessage());
        }
    }


    public WeeklySummary generateWeeklySummary(Long userId, LocalDateTime date) throws InstanceNotFoundException {

        Users user = userService.checkUser(userId); 

        int totalEntries = diaryEntryService.getDiaryEntriesByUserIdAndDate(userId, date.minusDays(7), date).size();
        
        if (totalEntries == 0) {
            return null;
        }

        int habitsCompleted = habitService.getUserHabitsAfterDate(userId, date.minusDays(7), date).size();
        int trophiesEarned = trophyService.getUserTrophiesByUserIdAndDate(userId, date).size();

        HabitEntry biggestStreak = habitService.getUserBiggestStreak(userId);
        int biggestStreakValue = 0;

        if (biggestStreak != null) {
            biggestStreakValue = biggestStreak.getStreak();
        }

        DiaryEntry mostFrequentMoodEntry = diaryEntryService.getWeeksMostFrequentMood(userId, date.minusDays(7), date);
        Mood moodTrend = null;
        
        if (mostFrequentMoodEntry != null && mostFrequentMoodEntry.getMood() != null) {
            moodTrend = mostFrequentMoodEntry.getMood();
        }

        return weeklySummaryDao.save(new WeeklySummary(habitsCompleted, totalEntries, trophiesEarned, biggestStreakValue, user, moodTrend, date));
    }

    public Page<WeeklySummary> getWeeklySummariesByUserId(Long userId, int page, int size) throws InstanceNotFoundException {
        userService.checkUser(userId);
        return weeklySummaryDao.findByUserIdOrderByDateDesc(userId, PageRequest.of(page, size));
    } 

    public WeeklySummary getWeeklySummaryById(Long id) throws InstanceNotFoundException {

        return weeklySummaryDao.findById(id).orElseThrow(() -> new InstanceNotFoundException("WeeklySummary not found", id));
    }
}
