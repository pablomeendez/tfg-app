package com.tfg.tfg_app.model.services;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.tfg_app.Application;
import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;
import com.tfg.tfg_app.model.entities.Category;
import com.tfg.tfg_app.model.entities.CategoryDao;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitDao;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.HabitEntryDao;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.MoodDao;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.entities.WeeklySummary;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Transactional
public class WeeklySummaryServiceTest {

    @Autowired
    private WeeklySummaryService weeklySummaryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DiaryEntryService diaryEntryService;
    
    @Autowired
    private HabitService habitService;
    
    @Autowired
    private MoodDao moodDao;
    
    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private HabitDao habitDao;
    
    @Autowired
    private HabitEntryDao habitEntryDao;
    
    private Long testUserId;
    private Mood testMood;
    private Category testCategory;
    private Habit testHabit;
    
    @Before
    public void setUp() throws DuplicateInstanceException, InstanceNotFoundException, DuplicatedEntryException, TrophyAlreadyGivenException {
        // Create test data
        Users testUser = new Users();
        testUser.setUserName("testUser");
        testUser.setPassword("$2a$10$tAX5UGkz3VvxhLe8.463oOuYMOXGFXB..pZzc2/sXXbOnJ2eWO2NO");
        testUser.setEmail("test@example.com");
        testUser.setName("Test");
        testUser.setLastName("User");
        testUser.setFirstEntry(false);
        testUser.setRole(Users.Role.USER);
        userService.signUp(testUser);
        testUserId = testUser.getId();
        
        // Create test mood
        testMood = new Mood();
        Map<String, String> moodNames = new HashMap<>();
        moodNames.put("en", "Happy");
        moodNames.put("es", "Feliz");
        moodNames.put("gl", "Feliz");
        testMood.setName(moodNames);
        testMood.setImage("happy.svg");
        testMood = moodDao.save(testMood);
        
        // Create test category
        testCategory = new Category();
        Map<String, String> categoryNames = new HashMap<>();
        categoryNames.put("en", "Health");
        categoryNames.put("es", "Salud");
        categoryNames.put("gl", "Saúde");
        testCategory.setName(categoryNames);
        testCategory = categoryDao.save(testCategory);
        
        // Create test habit
        testHabit = new Habit();
        Map<String, String> habitNames = new HashMap<>();
        habitNames.put("en", "Test Habit");
        habitNames.put("es", "Hábito de Prueba");
        habitNames.put("gl", "Hábito de Proba");
        testHabit.setName(habitNames);
        
        Map<String, String> habitDescriptions = new HashMap<>();
        habitDescriptions.put("en", "Test habit description");
        habitDescriptions.put("es", "Descripción del hábito de prueba");
        habitDescriptions.put("gl", "Descrición do hábito de proba");
        testHabit.setDescription(habitDescriptions);
        testHabit.setImage("test.jpg");
        testHabit.setCategory(testCategory);
        testHabit = habitDao.save(testHabit);
        
        // Create user habit and diary entry to have some data
        habitService.createUserHabit(testUserId, testHabit.getId());
        
        DiaryEntry diaryEntry = new DiaryEntry("Test diary entry", LocalDateTime.now(), testUser, testMood);
        diaryEntry = diaryEntryService.createDiaryEntry(testUser.getId(), diaryEntry, new ArrayList<>(), new ArrayList<>());
        
        HabitEntry habitEntry = new HabitEntry();
        habitEntry.setUser(testUser);
        habitEntry.setHabit(testHabit);
        habitEntry.setDiaryEntry(diaryEntry);
        habitEntry.setStreak(5);
        habitEntry.setDate(LocalDateTime.now());
        habitEntryDao.save(habitEntry);
    }

    @Test
    public void testGenerateWeeklySummary() throws InstanceNotFoundException {
        // Given
        LocalDateTime date = LocalDateTime.now();

        // When
        WeeklySummary result = weeklySummaryService.generateWeeklySummary(testUserId, date);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getHabitsCompleted() >= 0);
        assertTrue(result.getTotalEntries() >= 0);
        assertTrue(result.getTrophiesEarned() >= 0);
        assertNotNull(result.getBiggestStreak());
        assertEquals(testUserId, result.getUser().getId());
        assertNotNull(result.getMoodTrend());
        assertNotNull(result.getDate());
    }

    @Test(expected = InstanceNotFoundException.class)
    public void testGenerateWeeklySummaryWithNonExistentUser() throws InstanceNotFoundException {
        // Given
        Long userId = 999L;
        LocalDateTime date = LocalDateTime.now();

        // When & Then
        weeklySummaryService.generateWeeklySummary(userId, date);
    }

    @Test
    public void testGetWeeklySummariesByUserId() throws InstanceNotFoundException {
        // Given
        LocalDateTime date = LocalDateTime.now();
        
        // Generate a weekly summary first
        WeeklySummary createdSummary = weeklySummaryService.generateWeeklySummary(testUserId, date);

        // When
        List<WeeklySummary> result = weeklySummaryService.getWeeklySummariesByUserId(testUserId, 0, 10).getContent();

        // Then
        assertNotNull(result);
        assertTrue(result.size() >= 1);
        assertTrue(result.stream().anyMatch(summary -> summary.getId().equals(createdSummary.getId())));
        
        // Verify all summaries belong to the correct user
        for (WeeklySummary summary : result) {
            assertEquals(testUserId, summary.getUser().getId());
        }
    }

    @Test(expected = InstanceNotFoundException.class)
    public void testGetWeeklySummariesByUserIdWithNonExistentUser() throws InstanceNotFoundException {
        // Given
        Long userId = 999L;

        // When & Then
        weeklySummaryService.getWeeklySummariesByUserId(userId, 0, 10).getContent();
    }

    @Test
    public void testGetWeeklySummaryById() throws InstanceNotFoundException {
        // Given
        LocalDateTime date = LocalDateTime.now();
        
        // Generate summary first
        WeeklySummary createdSummary = weeklySummaryService.generateWeeklySummary(testUserId, date);
        Long summaryId = createdSummary.getId();

        // When
        WeeklySummary result = weeklySummaryService.getWeeklySummaryById(summaryId);

        // Then
        assertNotNull(result);
        assertEquals(summaryId, result.getId());
        assertEquals(testUserId, result.getUser().getId());
        assertEquals(createdSummary.getHabitsCompleted(), result.getHabitsCompleted());
        assertEquals(createdSummary.getTotalEntries(), result.getTotalEntries());
        assertEquals(createdSummary.getTrophiesEarned(), result.getTrophiesEarned());
    }

    @Test(expected = InstanceNotFoundException.class)
    public void testGetWeeklySummaryByIdWithNonExistentSummary() throws InstanceNotFoundException {
        // Given
        Long summaryId = 999L;

        // When & Then
        weeklySummaryService.getWeeklySummaryById(summaryId);
    }
}
