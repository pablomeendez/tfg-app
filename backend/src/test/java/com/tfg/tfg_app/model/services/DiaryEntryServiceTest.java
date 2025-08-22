package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

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
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;

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
    public void testCreateDiaryEntry() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException, TrophyAlreadyGivenException {
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
    public void testDeleteDiaryEntry() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry diaryEntry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        
        diaryEntryService.deleteDiaryEntry(diaryEntry);
        
        // Verify it's deleted by trying to find it
        assertThrows(InstanceNotFoundException.class, () -> diaryEntryService.getDiaryEntryById(diaryEntry.getId()));
    }

    @Test
    public void testDeleteDiaryEntryWithNonExistentId() {
        DiaryEntry nonExistentEntry = new DiaryEntry("content", LocalDateTime.now(), testUser, testMood);
        nonExistentEntry.setId(999L);
        
        assertThrows(InstanceNotFoundException.class, () -> diaryEntryService.deleteDiaryEntry(nonExistentEntry));
    }

    @Test
    public void testGetDiaryEntryById() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException, TrophyAlreadyGivenException {
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
    public void testGetDiaryEntriesByUserId() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException , TrophyAlreadyGivenException {
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
    public void testCreateMultipleDiaryEntriesForSameUser() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");

        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content1", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        assertThrows(DuplicatedEntryException.class, () ->
            diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("content2", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void testDiaryEntryPersistence() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime testDate = LocalDateTime.now();

        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Test Content", testDate, loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        DiaryEntry foundEntry = diaryEntryService.getDiaryEntryById(entry.getId());
        assertEquals("Test Content", foundEntry.getContent());
        assertEquals(loggedInUser.getId(), foundEntry.getUser().getId());
        assertEquals(testMood.getId(), foundEntry.getMood().getId());
    }

    @Test
    public void testCreateEntriesDifferentDates() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException {
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
    public void testCreateEntryAfterDeletingPrevious() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime sameDate = LocalDateTime.now().minusDays(5);
        
        DiaryEntry firstEntry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 1", sameDate, loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        
        diaryEntryService.deleteDiaryEntry(firstEntry);
        
        DiaryEntry newEntry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 2", sameDate, loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        assertNotNull(newEntry.getId());
    }

    @Test
    public void testCreateEntryWithVeryLongContent() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        String longContent = "This is a very long content that should be stored as TEXT in the database. ".repeat(100);
        
        assertThrows(DataIntegrityViolationException.class, () -> 
            diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry(longContent, LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void testCreateEntryWithSpecialCharacters() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");
        String specialContent = "Contenido con símbolos: @#$%^&*()_+-=[]{}|;':\",./<>?";
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry(specialContent, LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        assertEquals(specialContent, entry.getContent());
    }


    @Test
    public void testMultipleUsersCanCreateEntriesSameDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException {
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
    public void testCreateEntryWithPastDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime pastDate = LocalDateTime.now().minusDays(5);

        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content", pastDate, loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        assertEquals(pastDate.toLocalDate(), entry.getDate().toLocalDate());
    }

    @Test
    public void testCreateEntryWithFutureDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime futureDate = LocalDateTime.now().plusDays(5);

        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content", futureDate, loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        assertEquals(futureDate.toLocalDate(), entry.getDate().toLocalDate());
    }





    @Test
    public void testGetAllEntriesForUser() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");

        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 1", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 2", LocalDateTime.now().plusDays(1), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 3", LocalDateTime.now().plusDays(2), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        
        java.util.List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId(), 0, 10).getContent();
        assertEquals(3, entries.size());
    }

    @Test
    public void testDeleteAllEntriesForUser() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException, TrophyAlreadyGivenException {
        Users loggedInUser = userService.login("pablo", "1234");

        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 1", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(loggedInUser.getId(), new DiaryEntry("Content 2", LocalDateTime.now().plusDays(1), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());

        diaryEntryService.deleteDiaryEntry(entry1);
        diaryEntryService.deleteDiaryEntry(entry2);
        
        java.util.List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId(), 0, 10).getContent();
        assertEquals(0, entries.size());
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
    public void testDeleteDiaryEntryTwice() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry entry = diaryEntryService.createDiaryEntry(loggedInUser.getId(),
            new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, testMood), new ArrayList<>(), new ArrayList<>());
        diaryEntryService.deleteDiaryEntry(entry);
        assertThrows(InstanceNotFoundException.class, () -> diaryEntryService.deleteDiaryEntry(entry));
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

}
