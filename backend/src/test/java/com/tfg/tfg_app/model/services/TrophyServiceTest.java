package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

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
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;

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
        testCategory.setNameEn("Test Category");
        testCategory.setNameEs("Categoría de Prueba");
        testCategory.setNameGl("Categoría de Proba");
        testCategory = categoryDao.save(testCategory);

        testHabit = new Habit();
        testHabit.setNameEn("Test Habit");
        testHabit.setNameEs("Hábito de Prueba");
        testHabit.setNameGl("Hábito de Proba");
        testHabit.setDescriptionEn("Test habit description");
        testHabit.setDescriptionEs("Descripción del hábito de prueba");
        testHabit.setDescriptionGl("Descrición do hábito de proba");
        testHabit.setImage("test_habit.png");
        testHabit.setCategory(testCategory);
        testHabit = habitDao.save(testHabit);

        testTrophy7Days = new Trophy("Week Warrior", "Guerrero de la Semana", "Guerreiro da semana", "Complete 7 days", "Completar 7 días", "Completar 7 dias", 7, "trophy_7_days.png");
        testTrophy7Days = trophyDao.save(testTrophy7Days);

        testTrophy30Days = new Trophy("Monthly Master", "Maestro Mensual", "Mestre Mensual", "Complete 30 days", "Completar 30 días", "Completar 30 dias", 30, "trophy_30_days.png");
        testTrophy30Days = trophyDao.save(testTrophy30Days);
    }

    @Test
    public void testGetAllTrophies() {
        List<Trophy> trophies = trophyService.getAllTrophies();
        assertNotNull(trophies);
        assertTrue(trophies.size() >= 2);
        
        assertTrue(trophies.stream().anyMatch(t -> t.getNameEn().equals("Week Warrior")));
        assertTrue(trophies.stream().anyMatch(t -> t.getNameEn().equals("Monthly Master")));
    }

    @Test
    public void testGetTrophyById() throws InstanceNotFoundException {
        Trophy trophy = trophyService.getTrophyById(testTrophy7Days.getId());
        assertNotNull(trophy);
        assertEquals(testTrophy7Days.getId(), trophy.getId());
        assertEquals("Week Warrior", trophy.getNameEn());
        assertEquals("Complete 7 days", trophy.getDescriptionEn());
        assertEquals(7, trophy.getDays());
    }

    @Test
    public void testGetTrophyByIdNotFound() {
        assertThrows(InstanceNotFoundException.class, () -> {
            trophyService.getTrophyById(NON_EXISTENT_ID);
        });
    }

    @Test
    public void testCreateUserTrophy() throws InstanceNotFoundException, TrophyAlreadyGivenException  {
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
    public void testCreateUserTrophyAlreadyGiven() throws InstanceNotFoundException, TrophyAlreadyGivenException {
        trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 7);

        assertThrows(TrophyAlreadyGivenException.class, () -> {
            trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 7);
        });
    }

    @Test
    public void testCreateMultipleTrophiesForSameUserAndHabit() throws InstanceNotFoundException, TrophyAlreadyGivenException {
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
    public void testGetUserTrophies() throws InstanceNotFoundException, TrophyAlreadyGivenException {
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
    public void testGetUserTrophiesByUserIdAndHabitId() throws InstanceNotFoundException, TrophyAlreadyGivenException {
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

    @Test
    public void testGetTrophiesByUserId() throws InstanceNotFoundException, TrophyAlreadyGivenException {
        trophyService.checkAndAwardUserTrophy(testUser.getId(), testHabit.getId(), 7);

        List<Trophy> trophies = trophyService.getTrophiesByUserId(testUser.getId());
        assertNotNull(trophies);
        assertTrue(trophies.size() >= 0);
    }
}
