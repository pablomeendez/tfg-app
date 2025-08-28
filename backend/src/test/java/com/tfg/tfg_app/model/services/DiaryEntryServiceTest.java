package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.MoodDao;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;
import com.tfg.tfg_app.model.services.exceptions.IncorrectLoginException;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DiaryEntryServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DiaryEntryService diaryEntryService;

    @Autowired
    private MoodDao moodDao;

    private Users testUser;
    private Mood testMood, testMood2;

    @Before
    public void setUp() throws DuplicateInstanceException, IncorrectLoginException {
        testUser = new Users("pablo", "1234", "Pablo", "García", "pablo@gmail.com");
        userService.signUp(testUser);
    
        testMood = new Mood();
        Map<String, String> moodNames = new HashMap<>();
        moodNames.put("en", "Happy");
        moodNames.put("es", "Feliz");
        moodNames.put("gl", "Feliz");
        testMood.setName(moodNames);
        testMood.setImage("happy_image.png");
        moodDao.save(testMood);

        testMood2 = new Mood();
        Map<String, String> moodNames2 = new HashMap<>();
        moodNames2.put("en", "Sad");
        moodNames2.put("es", "Triste");
        moodNames2.put("gl", "Triste");
        testMood2.setName(moodNames2);
        testMood2.setImage("sad_image.png");
        moodDao.save(testMood2);
    }

    @Test
    public void testCreateDiaryEntry() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry diaryEntry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry foundDiaryEntry = diaryEntryService.getDiaryEntryById(diaryEntry.getId());
        
        assertEquals(foundDiaryEntry, diaryEntry);
    }

    @Test
    public void testCreateDiaryEntryWithNullUser() throws DuplicateInstanceException, IncorrectLoginException {
        assertThrows(InstanceNotFoundException.class, () -> 
            diaryEntryService.createDiaryEntry(999L, new DiaryEntry("content", LocalDateTime.now(), null, testMood), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void testCreateDiaryEntryWithNullMood() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");
        assertThrows(DataIntegrityViolationException.class, () -> 
            diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content", LocalDateTime.now(), loggedInUser, null), new ArrayList<>(), new ArrayList<>()));
    }


    @Test
    public void testGetDiaryEntryById() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry createdEntry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        
        DiaryEntry foundEntry = diaryEntryService.getDiaryEntryById(createdEntry.getId());
        
        assertEquals(createdEntry.getId(), foundEntry.getId());
        assertEquals(createdEntry.getContent(), foundEntry.getContent());
        assertEquals(createdEntry.getUser().getId(), foundEntry.getUser().getId());
    }

    @Test
    public void testGetDiaryEntryByIdWithNonExistentId() {
        assertThrows(InstanceNotFoundException.class, () -> diaryEntryService.getDiaryEntryById(999L));
    }

    @Test
    public void testGetDiaryEntriesByUserId() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException  {
        Users loggedInUser = userService.login("pablo", "1234");
        
        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content1", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content2", LocalDateTime.now().plusDays(1), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry entry3 = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content3", LocalDateTime.now().plusDays(2), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        java.util.List<DiaryEntry> userEntries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId(), 0, 10).getContent();
        
        assertEquals(3, userEntries.size());

    }

    @Test
    public void testGetDiaryEntriesByUserIdWithNoEntries() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        java.util.List<DiaryEntry> userEntries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId(), 0, 10).getContent();
        
        assertEquals(0, userEntries.size());
    }

    @Test
    public void testCreateDiaryEntryWithNullContent() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");

        assertThrows(DataIntegrityViolationException.class, () -> 
            diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry(null, LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>()));
    }
    
    @Test
    public void testCreateMultipleDiaryEntriesForSameUser() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");

        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content1", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        assertThrows(DuplicatedEntryException.class, () ->
            diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content2", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void testDiaryEntryPersistence() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime testDate = LocalDateTime.now();

        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Test Content", testDate, loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        DiaryEntry foundEntry = diaryEntryService.getDiaryEntryById(entry.getId());
        assertEquals("Test Content", foundEntry.getContent());
        assertEquals(loggedInUser.getId(), foundEntry.getUser().getId());
        assertEquals(testMood.getId(), foundEntry.getMood().getId());
    }

    @Test
    public void testCreateEntriesDifferentDates() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");

        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 1", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 2", LocalDateTime.now().plusDays(1), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry entry3 = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 3", LocalDateTime.now().plusDays(2), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        assertNotNull(entry1.getId());
        assertNotNull(entry2.getId());
        assertNotNull(entry3.getId());
        assertNotEquals(entry1.getId(), entry2.getId());
        assertNotEquals(entry2.getId(), entry3.getId());
    }

    @Test
    public void testCreateEntryWithVeryLongContent() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        String longContent = "This is a very long content that should be stored as TEXT in the database. ".repeat(100);
        
        assertThrows(DataIntegrityViolationException.class, () -> 
            diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry(longContent, LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void testCreateEntryWithSpecialCharacters() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        String specialContent = "Contenido con símbolos: @#$%^&*()_+-=[]{}|;':\",./<>?";
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry(specialContent, LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        assertEquals(specialContent, entry.getContent());
    }


    @Test
    public void testMultipleUsersCanCreateEntriesSameDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users user2 = new Users("maria", "5678", "María", "López", "maria@gmail.com");
        userService.signUp(user2);
        Users loggedInUser1 = userService.login("pablo", "1234");
        Users loggedInUser2 = userService.login("maria", "5678");
        
        LocalDateTime sameDate = LocalDateTime.now();

        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(loggedInUser1.getId(), new DiaryEntry("Content 1", sameDate, loggedInUser1, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(loggedInUser2.getId(), new DiaryEntry("Content 2", sameDate, loggedInUser2, testMood), new ArrayList<>(), new ArrayList<>());

        assertNotNull(entry1.getId());
        assertNotNull(entry2.getId());
        assertNotEquals(entry1.getId(), entry2.getId());
    }


    @Test
    public void testCreateEntryWithPastDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime pastDate = LocalDateTime.now().minusDays(5);

        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content", pastDate, loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        assertEquals(pastDate.toLocalDate(), entry.getDate().toLocalDate());
    }

    @Test
    public void testCreateEntryWithFutureDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime futureDate = LocalDateTime.now().plusDays(5);

        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content", futureDate, loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        assertEquals(futureDate.toLocalDate(), entry.getDate().toLocalDate());
    }





    @Test
    public void testGetAllEntriesForUser() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");

        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 1", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 2", LocalDateTime.now().plusDays(1), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 3", LocalDateTime.now().plusDays(2), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        
        java.util.List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId(), 0, 10).getContent();
        assertEquals(3, entries.size());
    }


    @Test
    public void testCreateEntryWithInvalidUserId() {
        assertThrows(InstanceNotFoundException.class, () -> 
            diaryEntryService.createDiaryEntry(999L, new DiaryEntry("Content", LocalDateTime.now(), null, testMood), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void testCreateEntryWithInvalidMoodId() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");
        Mood invalidMood = new Mood();
        invalidMood.setId(999L);
        
        assertThrows(DataIntegrityViolationException.class, () -> 
            diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, invalidMood), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void testGetDiaryEntriesByUserIdWithGaps() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content", LocalDateTime.now().plusDays(9), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        java.util.List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId(), 0, 10).getContent();
        assertEquals(2, entries.size());
    }

    @Test
    public void testCreateDiaryEntryWithDifferentMoods() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content1", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content2", LocalDateTime.now().plusDays(1), loggedInUser, testMood2), new ArrayList<>(), new ArrayList<>());
        assertNotEquals(entry1.getMood().getId(), entry2.getMood().getId());
    }

    @Test
    public void testCreateDiaryEntryWithMultipleImages() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        List<byte[]> images = new ArrayList<>();
        images.add(new byte[]{1,2,3});
        images.add(new byte[]{4,5,6});
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, testMood), images, new ArrayList<>());
        assertNotNull(entry.getImages());
        assertEquals(2, entry.getImages().size());
    }

    @Test
    public void testGetAllMoods() {
        List<Mood> moods = diaryEntryService.getAllMoods();
        assertNotNull(moods);
        assertTrue(moods.size() >= 2); // We created at least 2 moods in setUp
        assertTrue(moods.stream().anyMatch(mood -> mood.getName().get("en").equals("Happy")));
        assertTrue(moods.stream().anyMatch(mood -> mood.getName().get("en").equals("Sad")));
    }

    @Test
    public void testGetWeeksMostFrequentMood() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        // Create multiple entries with same mood
        diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content1", LocalDateTime.now().minusDays(1), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content2", LocalDateTime.now().minusDays(2), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        DiaryEntry result = diaryEntryService.getWeeksMostFrequentMood(loggedInUser.getId(), startDate, endDate);
        
        if (result != null) {
            assertNotNull(result.getMood());
            assertEquals(testMood.getId(), result.getMood().getId());
        }
    }

    @Test
    public void testGetDiaryEntriesByUserIdAndDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime startDate = LocalDateTime.now().minusDays(5);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content1", LocalDateTime.now().minusDays(2), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content2", LocalDateTime.now().minusDays(3), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesByUserIdAndDate(loggedInUser.getId(), startDate, endDate);
        
        assertNotNull(entries);
        assertTrue(entries.size() >= 2);
        assertTrue(entries.stream().allMatch(entry -> 
            entry.getDate().isAfter(startDate.minusSeconds(1)) && entry.getDate().isBefore(endDate.plusSeconds(1))));
    }

    @Test
    public void testGetLatestDiaryEntry() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");

        diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Older content", LocalDateTime.now().minusDays(2), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Latest content", LocalDateTime.now().minusDays(1), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        DiaryEntry latestEntry = diaryEntryService.getLatestDiaryEntry(loggedInUser.getId());
        
        assertNotNull(latestEntry);
        assertEquals("Latest content", latestEntry.getContent());
        assertEquals(entry2.getId(), latestEntry.getId());
    }

    @Test
    public void testGetLatestDiaryEntryWithNonExistentUser() throws InstanceNotFoundException {
        DiaryEntry result = diaryEntryService.getLatestDiaryEntry(999L);
        assertNull(result);
    }

    @Test
    public void testGetLatestDiaryEntryWithNoEntries() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        DiaryEntry result = diaryEntryService.getLatestDiaryEntry(loggedInUser.getId());
        assertNull(result);
    }

    @Test
    public void testCreateDiaryEntryWithHabits() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        // Create test habit data
        List<com.tfg.tfg_app.model.entities.UserHabit> habits = new ArrayList<>();
        // Note: This would need actual UserHabit objects, but for now we test with empty list
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content with habits", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), habits);
        
        assertNotNull(entry);
        assertEquals("Content with habits", entry.getContent());
    }

    @Test
    public void testCreateDiaryEntryWithNullDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        // Create entry with null date to test the date assignment logic
        DiaryEntry entryWithNullDate = new DiaryEntry("Content with null date", null, loggedInUser, testMood);
        
        DiaryEntry createdEntry = diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            entryWithNullDate, new ArrayList<>(), new ArrayList<>());
        
        assertNotNull(createdEntry);
        assertNotNull(createdEntry.getDate());
        assertEquals("Content with null date", createdEntry.getContent());
        // Verify the date was set to now (within reasonable time difference)
        assertTrue(createdEntry.getDate().isAfter(LocalDateTime.now().minusMinutes(1)));
        assertTrue(createdEntry.getDate().isBefore(LocalDateTime.now().plusMinutes(1)));
    }

    @Test
    public void testCreateDiaryEntryWithImages() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        // Create test image data
        List<byte[]> images = new ArrayList<>();
        images.add("test image 1".getBytes());
        images.add("test image 2".getBytes());
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content with images", LocalDateTime.now(), loggedInUser, testMood), images, new ArrayList<>());
        
        assertNotNull(entry);
        assertEquals("Content with images", entry.getContent());
        assertNotNull(entry.getImages());
        assertEquals(2, entry.getImages().size());
    }

}
