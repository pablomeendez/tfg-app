package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
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
import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitDao;
import com.tfg.tfg_app.model.entities.Trophy;
import com.tfg.tfg_app.model.entities.TrophyDao;
import com.tfg.tfg_app.model.entities.UserTrophy;
import com.tfg.tfg_app.model.entities.Users;
import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TrophyServiceTest {
    
    private final Long NON_EXISTENT_ID = Long.valueOf(-1);

    @Autowired
    private TrophyService trophyService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrophyDao trophyDao;

    @Autowired
    private HabitDao habitDao;

    @Autowired
    private CategoryDao categoryDao;

    private Users testUser;
    private Trophy testTrophy7Days;
    private Trophy testTrophy30Days;
    private Habit testHabit;
    private Category testCategory;

    @Before
    public void setUp() throws DuplicateInstanceException {
        testUser = new Users("testuser", "password123", "Test", "User", "test@example.com");
        userService.signUp(testUser);

        testCategory = new Category();
        Map<String, String> categoryNames = new HashMap<>();
        categoryNames.put("en", "Test Category");
        categoryNames.put("es", "Categoría de Prueba");
        categoryNames.put("gl", "Categoría de Proba");
        testCategory.setName(categoryNames);
        testCategory = categoryDao.save(testCategory);

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

        Map<String, String> trophy7Names = new HashMap<>();
        trophy7Names.put("en", "Week Warrior");
        trophy7Names.put("es", "Guerrero de la Semana");
        trophy7Names.put("gl", "Guerreiro da semana");
        
        Map<String, String> trophy7Descriptions = new HashMap<>();
        trophy7Descriptions.put("en", "Complete 7 days");
        trophy7Descriptions.put("es", "Completar 7 días");
        trophy7Descriptions.put("gl", "Completar 7 dias");
        
        testTrophy7Days = new Trophy(trophy7Names, trophy7Descriptions, 7, "trophy_7_days.png");
        testTrophy7Days = trophyDao.save(testTrophy7Days);

        Map<String, String> trophy30Names = new HashMap<>();
        trophy30Names.put("en", "Monthly Master");
        trophy30Names.put("es", "Maestro Mensual");
        trophy30Names.put("gl", "Mestre Mensual");
        
        Map<String, String> trophy30Descriptions = new HashMap<>();
        trophy30Descriptions.put("en", "Complete 30 days");
        trophy30Descriptions.put("es", "Completar 30 días");
        trophy30Descriptions.put("gl", "Completar 30 dias");
        
        testTrophy30Days = new Trophy(trophy30Names, trophy30Descriptions, 30, "trophy_30_days.png");
        testTrophy30Days = trophyDao.save(testTrophy30Days);
    }

    @Test
    public void testGetAllTrophies() {
        List<Trophy> trophies = trophyService.getAllTrophies();
        assertNotNull(trophies);
        assertTrue(trophies.size() >= 2);
        
        assertTrue(trophies.stream().anyMatch(t -> t.getName().get("en").equals("Week Warrior")));
        assertTrue(trophies.stream().anyMatch(t -> t.getName().get("en").equals("Monthly Master")));
    }

    @Test
    public void testCreateUserTrophy() throws InstanceNotFoundException  {
        UserTrophy userTrophy = trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 7);
        
        assertNotNull(userTrophy);
        assertNotNull(userTrophy.getId());
        assertEquals(testUser.getId(), userTrophy.getUser().getId());
        assertEquals(7, userTrophy.getTrophy().getDays());
        assertEquals(testHabit.getId(), userTrophy.getHabit().getId());
        assertNotNull(userTrophy.getObtainedAt());
        assertTrue(userTrophy.getObtainedAt().isBefore(LocalDateTime.now().plusMinutes(1)));
    }

    @Test
    public void testCreateUserTrophyWithInvalidUserId() {
        assertThrows(InstanceNotFoundException.class, () -> {
            trophyService.checkAndAwardUserTrophy(NON_EXISTENT_ID, testHabit.getId(), 7);
        });
    }

    @Test
    public void testCreateUserTrophyWithInvalidHabitId() {
        assertThrows(InstanceNotFoundException.class, () -> {
            trophyService.checkAndAwardUserTrophy(testUser.getId(), NON_EXISTENT_ID, 7);
        });
    }

    @Test
    public void testCreateUserTrophyWithInvalidDays() {
        assertThrows(IllegalArgumentException.class, () -> {
            trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), -5);
        });
    }

    @Test
    public void testCreateMultipleTrophiesForSameUserAndHabit() throws InstanceNotFoundException {
        UserTrophy userTrophy7 = trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 7);
        assertNotNull(userTrophy7);
        assertEquals(7, userTrophy7.getTrophy().getDays());

        UserTrophy userTrophy30 = trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 30);
        assertNotNull(userTrophy30);
        assertEquals(30, userTrophy30.getTrophy().getDays());
        
        List<UserTrophy> userTrophies = trophyService.getUserTrophiesByUserIdAndHabitId(testUser.getId(), testHabit.getId());
        assertEquals(2, userTrophies.size());
    }

    @Test
    public void testGetUserTrophies() throws InstanceNotFoundException {
        List<UserTrophy> initialTrophies = trophyService.getUserTrophies(testUser.getId());
        int initialCount = initialTrophies.size();

        trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 7);

        List<UserTrophy> updatedTrophies = trophyService.getUserTrophies(testUser.getId());
        assertEquals(initialCount + 1, updatedTrophies.size());
        
        UserTrophy userTrophy = updatedTrophies.stream()
            .filter(ut -> ut.getTrophy().getDays() == 7)
            .findFirst()
            .orElse(null);
        assertNotNull(userTrophy);
        assertEquals(testUser.getId(), userTrophy.getUser().getId());
    }

    @Test
    public void testGetUserTrophiesWithInvalidUserId() {
        assertThrows(InstanceNotFoundException.class, () -> {
            trophyService.getUserTrophies(NON_EXISTENT_ID);
        });
    }

    @Test
    public void testGetUserTrophiesByUserIdAndHabitId() throws InstanceNotFoundException {
        List<UserTrophy> initialTrophies = trophyService.getUserTrophiesByUserIdAndHabitId(testUser.getId(), testHabit.getId());
        assertEquals(0, initialTrophies.size());
        trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 7);
        trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 30);

        List<UserTrophy> trophies = trophyService.getUserTrophiesByUserIdAndHabitId(testUser.getId(), testHabit.getId());
        assertEquals(2, trophies.size());
        
        assertTrue(trophies.stream().anyMatch(ut -> ut.getTrophy().getDays() == 7));
        assertTrue(trophies.stream().anyMatch(ut -> ut.getTrophy().getDays() == 30));
        assertTrue(trophies.stream().allMatch(ut -> ut.getHabit().getId().equals(testHabit.getId())));
    }

    @Test
    public void testGetUserTrophiesByUserIdAndHabitIdWithInvalidUserId() {
        assertThrows(InstanceNotFoundException.class, () -> {
            trophyService.getUserTrophiesByUserIdAndHabitId(NON_EXISTENT_ID, testHabit.getId());
        });
    }
}
