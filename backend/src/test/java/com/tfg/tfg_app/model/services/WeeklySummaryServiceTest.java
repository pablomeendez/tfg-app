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
import com.tfg.tfg_app.model.entities.Category;
import com.tfg.tfg_app.model.entities.CategoryDao;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.DiaryEntryDao;
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
    
    @Autowired
    private DiaryEntryDao diaryEntryDao;
    
    private Long testUserId;
    private Mood testMood;
    private Category testCategory;
    private Habit testHabit;
    
    @Before
    public void setUp() throws DuplicateInstanceException, InstanceNotFoundException, DuplicatedEntryException {
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

    @Test
    public void testWeeklySummaryWithEmptyData() throws InstanceNotFoundException, DuplicateInstanceException {
        // Create a new user without any data
        Users newUser = new Users();
        newUser.setUserName("emptyUser");
        newUser.setPassword("$2a$10$tAX5UGkz3VvxhLe8.463oOuYMOXGFXB..pZzc2/sXXbOnJ2eWO2NO");
        newUser.setEmail("empty@example.com");
        newUser.setName("Empty");
        newUser.setLastName("User");
        newUser.setFirstEntry(false);
        newUser.setRole(Users.Role.USER);
        userService.signUp(newUser);

        // Create a default mood for the test
        Mood defaultMood = new Mood();
        Map<String, String> moodName = new HashMap<>();
        moodName.put("en", "Neutral");
        moodName.put("es", "Neutral");
        moodName.put("gl", "Neutral");
        defaultMood.setName(moodName);
        defaultMood.setImage("neutral.png");
        moodDao.save(defaultMood);

        // Create a diary entry so there's some data for mood trend
        DiaryEntry entry = new DiaryEntry();
        entry.setContent("Test entry for empty data");
        entry.setDate(LocalDateTime.now().minusDays(1));
        entry.setUser(newUser);
        entry.setMood(defaultMood);
        diaryEntryDao.save(entry);

        // Generate weekly summary for user with minimal data
        WeeklySummary result = weeklySummaryService.generateWeeklySummary(newUser.getId(), LocalDateTime.now());

        assertNotNull(result);
        assertEquals(0, result.getHabitsCompleted());
        assertEquals(1, result.getTotalEntries()); // Should have 1 entry now
        assertEquals(0, result.getTrophiesEarned());
        assertNotNull(result.getMoodTrend());
    }

    @Test
    public void testMultipleWeeklySummariesPagination() throws InstanceNotFoundException {
        LocalDateTime date = LocalDateTime.now();
        
        // Create a default mood for the test
        Mood defaultMood = new Mood();
        Map<String, String> moodName = new HashMap<>();
        moodName.put("en", "Happy");
        moodName.put("es", "Feliz");
        moodName.put("gl", "Feliz");
        defaultMood.setName(moodName);
        defaultMood.setImage("happy.png");
        moodDao.save(defaultMood);

        // Create diary entries for the test user so there's data for mood trend
        Users testUser = userService.checkUser(testUserId);
        DiaryEntry entry1 = new DiaryEntry();
        entry1.setContent("Test entry 1");
        entry1.setDate(date.minusDays(1));
        entry1.setUser(testUser);
        entry1.setMood(defaultMood);
        diaryEntryDao.save(entry1);

        DiaryEntry entry2 = new DiaryEntry();
        entry2.setContent("Test entry 2");
        entry2.setDate(date.minusDays(8));
        entry2.setUser(testUser);
        entry2.setMood(defaultMood);
        diaryEntryDao.save(entry2);

        DiaryEntry entry3 = new DiaryEntry();
        entry3.setContent("Test entry 3");
        entry3.setDate(date.minusDays(15));
        entry3.setUser(testUser);
        entry3.setMood(defaultMood);
        diaryEntryDao.save(entry3);
        
        // Generate multiple weekly summaries
        weeklySummaryService.generateWeeklySummary(testUserId, date);
        weeklySummaryService.generateWeeklySummary(testUserId, date.minusDays(7));
        weeklySummaryService.generateWeeklySummary(testUserId, date.minusDays(14));

        // Test pagination
        List<WeeklySummary> firstPage = weeklySummaryService.getWeeklySummariesByUserId(testUserId, 0, 2).getContent();
        List<WeeklySummary> secondPage = weeklySummaryService.getWeeklySummariesByUserId(testUserId, 1, 2).getContent();
        
        assertNotNull(firstPage);
        assertNotNull(secondPage);
        assertEquals(2, firstPage.size());
        assertTrue(secondPage.size() >= 1);
    }

    @Test
    public void testWeeklySummaryDataIntegrity() throws InstanceNotFoundException {
        LocalDateTime date = LocalDateTime.now();
        
        // Create a default mood for the test
        Mood defaultMood = new Mood();
        Map<String, String> moodName = new HashMap<>();
        moodName.put("en", "Content");
        moodName.put("es", "Contento");
        moodName.put("gl", "Contento");
        defaultMood.setName(moodName);
        defaultMood.setImage("content.png");
        moodDao.save(defaultMood);

        // Create diary entry for the test user
        Users testUser = userService.checkUser(testUserId);
        DiaryEntry entry = new DiaryEntry();
        entry.setContent("Test entry for data integrity");
        entry.setDate(date.minusDays(1));
        entry.setUser(testUser);
        entry.setMood(defaultMood);
        diaryEntryDao.save(entry);
        
        WeeklySummary summary = weeklySummaryService.generateWeeklySummary(testUserId, date);
        
        // Verify all required fields are present
        assertNotNull(summary.getUser());
        assertNotNull(summary.getDate());
        assertNotNull(summary.getMoodTrend());
        assertEquals(testUserId, summary.getUser().getId());
        
        // Verify numerical values are non-negative
        assertTrue(summary.getHabitsCompleted() >= 0);
        assertTrue(summary.getTotalEntries() >= 0);
        assertTrue(summary.getTrophiesEarned() >= 0);
        assertTrue(summary.getBiggestStreak() >= 0);
    }

    @Test
    public void testGenerateWeeklySummaryWithInvalidUser() {
        // Test edge case with non-existent user
        assertThrows(InstanceNotFoundException.class, () -> {
            weeklySummaryService.generateWeeklySummary(-1L, LocalDateTime.now());
        });
    }

    @Test
    public void testGetWeeklySummariesByUserIdWithInvalidUser() {
        // Test edge case with non-existent user for retrieving summaries
        assertThrows(InstanceNotFoundException.class, () -> {
            weeklySummaryService.getWeeklySummariesByUserId(-1L, 0, 10);
        });
    }

    @Test
    public void testGetWeeklySummariesWithZeroPageSize() throws InstanceNotFoundException {
        // Test edge case with zero page size - should throw exception
        assertThrows(IllegalArgumentException.class, () -> {
            weeklySummaryService.getWeeklySummariesByUserId(testUserId, 0, 0);
        });
    }
}
