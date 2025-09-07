package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AssistantServiceTest {

    private final Long NON_EXISTENT_ID = Long.valueOf(-1);

    @MockBean
    private Assistant assistant;

    @Autowired
    private AssistantService assistantService;

    @Autowired
    private UserService userService;

    @Autowired
    private WeeklySummaryService weeklySummaryService;

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

    private Users testUser;
    private Mood testMood;
    private Category testCategory;
    private Habit testHabit;

    @Before
    public void setUp() throws DuplicateInstanceException, InstanceNotFoundException, DuplicatedEntryException {
        testUser = new Users("testuser", "password123", "Test", "User", "test@example.com");
        userService.signUp(testUser);

        testMood = new Mood();
        Map<String, String> moodNames = new HashMap<>();
        moodNames.put("en", "Happy");
        moodNames.put("es", "Feliz");
        moodNames.put("gl", "Feliz");
        testMood.setName(moodNames);
        testMood.setImage("happy.svg");
        testMood = moodDao.save(testMood);

        testCategory = new Category();
        Map<String, String> categoryNames = new HashMap<>();
        categoryNames.put("en", "Health");
        categoryNames.put("es", "Salud");
        categoryNames.put("gl", "Saúde");
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

        // Create some test data for weekly summary
        habitService.createUserHabit(testUser.getId(), testHabit.getId());
        
        DiaryEntry diaryEntry = new DiaryEntry("Test diary entry", LocalDateTime.now(), testUser, testMood);
        diaryEntry = diaryEntryService.createDiaryEntry(testUser.getId(), diaryEntry, new ArrayList<>(), new ArrayList<>());

        HabitEntry habitEntry = new HabitEntry();
        habitEntry.setUser(testUser);
        habitEntry.setHabit(testHabit);
        habitEntry.setDiaryEntry(diaryEntry);
        habitEntry.setStreak(5);
        habitEntry.setDate(LocalDateTime.now());
        habitEntryDao.save(habitEntry);

        // Generate weekly summary for the user
        weeklySummaryService.generateWeeklySummary(testUser.getId(), LocalDateTime.now());

        // Mock the Assistant to return a test response
        ChatResponse mockResponse = ChatResponse.builder()
                .aiMessage(AiMessage.from("Test response from assistant"))
                .build();
        when(assistant.chat(anyLong(), anyString())).thenReturn(mockResponse);
    }

    @Test
    public void testChatWithWeeklySummary() throws InstanceNotFoundException, JsonProcessingException {
        String question = "How am I doing with my habits?";
        
        ChatResponse response = assistantService.chat(testUser.getId(), question);
        assertNotNull(response);
        assertNotNull(response.aiMessage());
        assertEquals("Test response from assistant", response.aiMessage().text());
    }

    @Test
    public void testChatWithNonExistentUser() throws InstanceNotFoundException, JsonProcessingException {
        String question = "Test question";

        ChatResponse response = assistantService.chat(NON_EXISTENT_ID, question);
        assertNotNull(response);
        assertNotNull(response.aiMessage());
        assertEquals("Test response from assistant", response.aiMessage().text());
    }
}
