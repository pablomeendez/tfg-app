package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Category;
import com.tfg.tfg_app.model.entities.CategoryDao;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitDao;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.HabitEntryDao;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.MoodDao;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class HabitServiceTest {
    


    @Autowired
    private HabitService habitService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiaryEntryService diaryEntryService;

    @Autowired
    private HabitDao habitDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private HabitEntryDao habitEntryDao;

    @Autowired
    private MoodDao moodDao;

    private Users testUser;
    private Category testCategory;
    private Habit testHabit;
    private DiaryEntry testDiaryEntry;
    private Mood testMood;

    @Before
    public void setUp() throws DuplicateInstanceException, DuplicatedEntryException, InstanceNotFoundException {
        testUser = new Users("testuser", "password123", "Test", "User", "test@example.com");
        userService.signUp(testUser);

        testCategory = new Category();
        Map<String, String> categoryNames = new HashMap<>();
        categoryNames.put("en", "Health");
        categoryNames.put("es", "Salud");
        categoryNames.put("gl", "Saúde");
        testCategory.setName(categoryNames);
        categoryDao.save(testCategory);

        testHabit = new Habit();
        Map<String, String> habitNames = new HashMap<>();
        habitNames.put("en", "Exercise");
        habitNames.put("es", "Ejercicio");
        habitNames.put("gl", "Exercicio");
        testHabit.setName(habitNames);
        
        Map<String, String> habitDescriptions = new HashMap<>();
        habitDescriptions.put("en", "Daily exercise routine");
        habitDescriptions.put("es", "Rutina de ejercicio diario");
        habitDescriptions.put("gl", "Rutina de exercicio diario");
        testHabit.setDescription(habitDescriptions);
        testHabit.setCategory(testCategory);
        habitDao.save(testHabit);

        testMood = new Mood();
        Map<String, String> moodNames = new HashMap<>();
        moodNames.put("en", "Happy");
        moodNames.put("es", "Feliz");
        moodNames.put("gl", "Feliz");
        testMood.setName(moodNames);
        testMood.setImage("happy_image.png");
        moodDao.save(testMood);

        testDiaryEntry = new DiaryEntry("Test diary entry", LocalDateTime.now(), testUser, testMood);
        testDiaryEntry = diaryEntryService.createDiaryEntry(testUser.getId(), testDiaryEntry, new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testGetAllHabits() {
        List<Habit> habits = habitService.getAllHabits();
        assertNotNull(habits);
        assertTrue(habits.size() >= 1);
        assertTrue(habits.contains(testHabit));
    }

    @Test
    public void testCreateUserHabit() throws InstanceNotFoundException {
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        assertNotNull(userHabit);
        assertNotNull(userHabit.getId());
        assertEquals(testUser.getId(), userHabit.getUser().getId());
        assertEquals(testHabit.getId(), userHabit.getHabit().getId());
    }

    @Test
    public void testGetHabitsByUserId() throws InstanceNotFoundException {
        // Crear algunos hábitos de usuario
        UserHabit userHabit1 = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        // Crear otro hábito
        Habit testHabit2 = new Habit();
        Map<String, String> habit2Names = new HashMap<>();
        habit2Names.put("en", "Meditation");
        habit2Names.put("es", "Meditación");
        habit2Names.put("gl", "Meditación");
        testHabit2.setName(habit2Names);
        
        Map<String, String> habit2Descriptions = new HashMap<>();
        habit2Descriptions.put("en", "Daily meditation");
        habit2Descriptions.put("es", "Meditación diaria");
        habit2Descriptions.put("gl", "Meditación diaria");
        testHabit2.setDescription(habit2Descriptions);
        testHabit2.setCategory(testCategory);
        habitDao.save(testHabit2);
        
        UserHabit userHabit2 = habitService.createUserHabit(testUser.getId(), testHabit2.getId());

        List<UserHabit> userHabits = habitService.getHabitsByUserId(testUser.getId());
        
        assertNotNull(userHabits);
        assertEquals(2, userHabits.size());
        assertTrue(userHabits.contains(userHabit1));
        assertTrue(userHabits.contains(userHabit2));
    }



    @Test
    public void testCreateHabitEntry() throws InstanceNotFoundException {
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        HabitEntry habitEntry = habitService.createHabitEntry(testUser.getId(), userHabit.getId(), testDiaryEntry.getId());
        
        assertNotNull(habitEntry);
        assertNotNull(habitEntry.getId());
        assertEquals(testUser.getId(), habitEntry.getUser().getId());
        assertEquals(testHabit.getId(), habitEntry.getHabit().getId());
        assertNotNull(habitEntry.getDate());
        assertEquals(1, habitEntry.getStreak()); // Primer entry, streak debería ser 1
    }

    @Test
    public void testCreateHabitEntryWithStreak() throws InstanceNotFoundException {
        // Primero crear el UserHabit
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        // Crear primera entrada (ayer)
        HabitEntry yesterdayEntry = new HabitEntry();
        yesterdayEntry.setUser(testUser);
        yesterdayEntry.setHabit(testHabit);
        yesterdayEntry.setDate(LocalDateTime.now().minusDays(1));
        yesterdayEntry.setStreak(5);
        yesterdayEntry.setDiaryEntry(testDiaryEntry);
        habitEntryDao.save(yesterdayEntry);

        // Crear entrada de hoy
        HabitEntry todayEntry = habitService.createHabitEntry(testUser.getId(), userHabit.getId(), testDiaryEntry.getId());
        
        assertNotNull(todayEntry);
        assertEquals(6, todayEntry.getStreak()); // Debería incrementar el streak
    }

    @Test
    public void testCreateHabitEntryWithoutConsecutiveDays() throws InstanceNotFoundException {
        // Primero crear el UserHabit
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        // Crear entrada hace dos días
        HabitEntry oldEntry = new HabitEntry();
        oldEntry.setUser(testUser);
        oldEntry.setHabit(testHabit);
        oldEntry.setDate(LocalDateTime.now().minusDays(2));
        oldEntry.setStreak(5);
        oldEntry.setDiaryEntry(testDiaryEntry);
        habitEntryDao.save(oldEntry);

        HabitEntry todayEntry = habitService.createHabitEntry(testUser.getId(), userHabit.getId(), testDiaryEntry.getId());
        
        assertNotNull(todayEntry);
        assertEquals(1, todayEntry.getStreak()); // Debería reiniciar el streak
    }



    @Test
    public void testDeleteUserHabit() throws InstanceNotFoundException {
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        assertNotNull(userHabit.getId());

        habitService.deleteUserHabit(userHabit.getId());

        // Verify habit was deleted by checking it's not in the user's habits
        List<UserHabit> userHabits = habitService.getHabitsByUserId(testUser.getId());
        assertFalse(userHabits.stream().anyMatch(uh -> uh.getId().equals(userHabit.getId())));
    }



    @Test
    public void testGetHabitEntriesByUserIdAndHabitId() throws InstanceNotFoundException, DuplicatedEntryException {
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        // Create multiple habit entries
        HabitEntry entry1 = habitService.createHabitEntry(testUser.getId(), userHabit.getId(), testDiaryEntry.getId());
        
        // Create another diary entry for the next day
        DiaryEntry diaryEntry2 = new DiaryEntry("Test diary entry 2", LocalDateTime.now().plusDays(1), testUser, testMood);
        diaryEntry2 = diaryEntryService.createDiaryEntry(testUser.getId(), diaryEntry2, new ArrayList<>(), new ArrayList<>());
        
        HabitEntry entry2 = habitService.createHabitEntry(testUser.getId(), userHabit.getId(), diaryEntry2.getId());

        List<HabitEntry> habitEntries = habitService.getHabitEntriesByUserIdAndHabitId(testUser.getId(), testHabit.getId());
        
        assertNotNull(habitEntries);
        assertTrue(habitEntries.size() >= 2);
        assertTrue(habitEntries.stream().anyMatch(he -> he.getId().equals(entry1.getId())));
        assertTrue(habitEntries.stream().anyMatch(he -> he.getId().equals(entry2.getId())));
    }

    @Test
    public void testGetUserBiggestStreak() throws InstanceNotFoundException {
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        // Create habit entry with a streak
        HabitEntry habitEntry = habitService.createHabitEntry(testUser.getId(), userHabit.getId(), testDiaryEntry.getId());
        
        HabitEntry biggestStreak = habitService.getUserBiggestStreak(testUser.getId());
        
        assertNotNull(biggestStreak);
        assertEquals(habitEntry.getStreak(), biggestStreak.getStreak());
    }


    @Test
    public void testGetUserHabitsAfterDate() throws InstanceNotFoundException {
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        
        // Create habit entry
        habitService.createHabitEntry(testUser.getId(), userHabit.getId(), testDiaryEntry.getId());

        List<HabitEntry> habitEntries = habitService.getUserHabitsAfterDate(testUser.getId(), startDate, endDate);
        
        assertNotNull(habitEntries);
        assertTrue(habitEntries.size() >= 1);
        assertTrue(habitEntries.stream().allMatch(he -> 
            he.getDate().isAfter(startDate) || he.getDate().isEqual(startDate)));
    }

    @Test
    public void testDeleteHabitEntry() throws InstanceNotFoundException {
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        HabitEntry habitEntry = habitService.createHabitEntry(testUser.getId(), userHabit.getId(), testDiaryEntry.getId());
        
        HabitEntry deletedEntry = habitService.deleteHabitEntry(testUser.getId(), habitEntry.getId());
        
        assertNotNull(deletedEntry);
        assertEquals(habitEntry.getId(), deletedEntry.getId());
    }

    @Test
    public void testGetUserBiggestStreakWithNoHabits() throws InstanceNotFoundException, DuplicateInstanceException {
        // Create a new user with no habit entries
        Users userWithNoHabits = new Users("nohabits", "password123", "No", "Habits", "nohabits@example.com");
        userService.signUp(userWithNoHabits);
        
        HabitEntry biggestStreak = habitService.getUserBiggestStreak(userWithNoHabits.getId());
        
        // Should return null when user has no habit entries
        assertEquals(null, biggestStreak);
    }

    @Test
    public void testGetHabitsByUserIdWithNoHabits() throws DuplicateInstanceException, InstanceNotFoundException {
        // Create a user with no habits
        Users userWithNoHabits = new Users("nohabits2", "password123", "No", "Habits2", "nohabits2@example.com");
        userService.signUp(userWithNoHabits);
        
        List<UserHabit> habits = habitService.getHabitsByUserId(userWithNoHabits.getId());
        assertNotNull(habits);
        assertTrue("New user should have no habits", habits.isEmpty());
    }

    @Test
    public void testCreateDuplicateUserHabit() throws DuplicateInstanceException, InstanceNotFoundException {
        // Create user habit first
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        assertNotNull(userHabit);
        
        // Try to create duplicate - test that it doesn't crash the system
        // The service may handle this by returning the existing habit or creating a new one
        UserHabit duplicateHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        assertNotNull(duplicateHabit);
        
        // Verify both habits are related to the same user and habit
        assertEquals(testUser.getId(), duplicateHabit.getUser().getId());
        assertEquals(testHabit.getId(), duplicateHabit.getHabit().getId());
    }

    @Test
    public void testGetHabitEntriesByUserIdAndHabitIdWithNoEntries() throws DuplicateInstanceException, InstanceNotFoundException {
        // Create a user habit but no entries
        habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        List<HabitEntry> entries = habitService.getHabitEntriesByUserIdAndHabitId(testUser.getId(), testHabit.getId());
        assertNotNull(entries);
        assertTrue("Should return empty list when no entries exist", entries.isEmpty());
    }

    @Test
    public void testGetUserHabitsAfterDateWithInvalidDateRange() throws DuplicateInstanceException, InstanceNotFoundException {
        // Test with end date before start date
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().minusDays(5); // End before start
        
        List<HabitEntry> entries = habitService.getUserHabitsAfterDate(testUser.getId(), startDate, endDate);
        assertNotNull(entries);
        assertTrue("Should return empty list for invalid date range", entries.isEmpty());
    }
}
