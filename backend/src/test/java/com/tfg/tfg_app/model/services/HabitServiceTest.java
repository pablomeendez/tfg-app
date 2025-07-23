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
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.HabitEntryDao;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.entities.Users;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class HabitServiceTest {
    
    private final Long NON_EXISTENT_ID = Long.valueOf(-1);

    @Autowired
    private HabitService habitService;

    @Autowired
    private UserService userService;

    @Autowired
    private HabitDao habitDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private HabitEntryDao habitEntryDao;

    private Users testUser;
    private Category testCategory;
    private Habit testHabit;

    @Before
    public void setUp() throws DuplicateInstanceException {
        // Crear usuario de prueba
        testUser = new Users("testuser", "password123", "Test", "User", "test@example.com");
        userService.signUp(testUser);

        // Crear categoría de prueba
        testCategory = new Category();
        testCategory.setName("Health");
        categoryDao.save(testCategory);

        // Crear hábito de prueba
        testHabit = new Habit();
        testHabit.setName("Exercise");
        testHabit.setDescription("Daily exercise routine");
        testHabit.setImage(new byte[]{1, 2, 3, 4});
        testHabit.setCategory(testCategory);
        habitDao.save(testHabit);
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
    public void testCreateUserHabitWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class, 
            () -> habitService.createUserHabit(NON_EXISTENT_ID, testHabit.getId()));
    }

    @Test
    public void testCreateUserHabitWithNonExistentHabit() {
        assertThrows(InstanceNotFoundException.class, 
            () -> habitService.createUserHabit(testUser.getId(), NON_EXISTENT_ID));
    }

    @Test
    public void testGetHabitsByUserId() throws InstanceNotFoundException {
        // Crear algunos hábitos de usuario
        UserHabit userHabit1 = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        // Crear otro hábito
        Habit testHabit2 = new Habit();
        testHabit2.setName("Meditation");
        testHabit2.setDescription("Daily meditation");
        testHabit2.setImage(new byte[]{5, 6, 7, 8});
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
    public void testGetHabitsByUserIdWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class, 
            () -> habitService.getHabitsByUserId(NON_EXISTENT_ID));
    }

    @Test
    public void testCreateHabitEntry() throws InstanceNotFoundException {
        // Primero crear el UserHabit
        UserHabit userHabit = habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        HabitEntry habitEntry = habitService.createHabitEntry(testUser.getId(), userHabit.getId());
        
        assertNotNull(habitEntry);
        assertNotNull(habitEntry.getId());
        assertEquals(testUser.getId(), habitEntry.getUser().getId());
        assertEquals(userHabit.getId(), habitEntry.getUserHabit().getId());
        assertEquals(testHabit.getId(), habitEntry.getUserHabit().getHabit().getId());
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
        yesterdayEntry.setUserHabit(userHabit);
        yesterdayEntry.setDate(LocalDateTime.now().minusDays(1));
        yesterdayEntry.setStreak(5);
        habitEntryDao.save(yesterdayEntry);

        // Crear entrada de hoy
        HabitEntry todayEntry = habitService.createHabitEntry(testUser.getId(), userHabit.getId());
        
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
        oldEntry.setUserHabit(userHabit);
        oldEntry.setDate(LocalDateTime.now().minusDays(2));
        oldEntry.setStreak(5);
        habitEntryDao.save(oldEntry);

        // Crear entrada de hoy
        HabitEntry todayEntry = habitService.createHabitEntry(testUser.getId(), userHabit.getId());
        
        assertNotNull(todayEntry);
        assertEquals(1, todayEntry.getStreak()); // Debería reiniciar el streak
    }

    @Test
    public void testCreateHabitEntryWithNonExistentUser() {
        assertThrows(InstanceNotFoundException.class, 
            () -> habitService.createHabitEntry(NON_EXISTENT_ID, NON_EXISTENT_ID));
    }

    @Test
    public void testCreateHabitEntryWithNonExistentUserHabit() {
        assertThrows(InstanceNotFoundException.class, 
            () -> habitService.createHabitEntry(testUser.getId(), NON_EXISTENT_ID));
    }
}
