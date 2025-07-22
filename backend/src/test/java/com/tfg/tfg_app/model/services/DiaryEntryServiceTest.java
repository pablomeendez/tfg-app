package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
import com.tfg.tfg_app.model.entities.Images;
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
        testMood.setName("Happy");
        testMood.setImage(new byte[]{1, 2, 3, 4});
        moodDao.save(testMood);

        testMood2 = new Mood();
        testMood2.setName("Sad");
        testMood2.setImage(new byte[]{1, 2, 3, 4});
        moodDao.save(testMood2);
    }

    @Test
    public void testCreateDiaryEntry() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry diaryEntry = diaryEntryService.createDiaryEntry(new DiaryEntry("content", LocalDateTime.now(), loggedInUser, testMood, null));
        DiaryEntry foundDiaryEntry = diaryEntryService.getDiaryEntryById(diaryEntry.getId());
        
        assertEquals(foundDiaryEntry, diaryEntry);
    }

    @Test
    public void testCreateDiaryEntryWithNullUser() throws DuplicateInstanceException, IncorrectLoginException {
        assertThrows(DataIntegrityViolationException.class, () -> diaryEntryService.createDiaryEntry(new DiaryEntry("content", LocalDateTime.now(), null, testMood, null)));
    }

    @Test
    public void testCreateDiaryEntryWithNullMood() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");
        assertThrows(DataIntegrityViolationException.class, () -> diaryEntryService.createDiaryEntry(new DiaryEntry("content", LocalDateTime.now(), loggedInUser, null, null)));
    }

    @Test
    public void testUpdateDiaryEntry() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry diaryEntry = diaryEntryService.createDiaryEntry(new DiaryEntry("content", LocalDateTime.now(), loggedInUser, testMood, null));
        
        diaryEntry.setContent("Updated Content");
        
        DiaryEntry updatedEntry = diaryEntryService.updateDiaryEntry(diaryEntry);
    
        assertEquals("Updated Content", updatedEntry.getContent());
    }

    @Test
    public void testUpdateDiaryEntryWithNonExistentId() {
        DiaryEntry nonExistentEntry = new DiaryEntry("content", LocalDateTime.now(), testUser, testMood, null);
        nonExistentEntry.setId(999L);
        
        assertThrows(InstanceNotFoundException.class, () -> diaryEntryService.updateDiaryEntry(nonExistentEntry));
    }

    @Test
    public void testDeleteDiaryEntry() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry diaryEntry = diaryEntryService.createDiaryEntry(new DiaryEntry("content", LocalDateTime.now(), loggedInUser, testMood, null));
        
        diaryEntryService.deleteDiaryEntry(diaryEntry);
        
        // Verify it's deleted by trying to find it
        assertThrows(InstanceNotFoundException.class, () -> diaryEntryService.getDiaryEntryById(diaryEntry.getId()));
    }

    @Test
    public void testDeleteDiaryEntryWithNonExistentId() {
        DiaryEntry nonExistentEntry = new DiaryEntry("content", LocalDateTime.now(), testUser, testMood, null);
        nonExistentEntry.setId(999L);
        
        assertThrows(InstanceNotFoundException.class, () -> diaryEntryService.deleteDiaryEntry(nonExistentEntry));
    }

    @Test
    public void testGetDiaryEntryById() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry createdEntry = diaryEntryService.createDiaryEntry(new DiaryEntry("content", LocalDateTime.now(), loggedInUser, testMood, null));
        
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
    public void testGetDiaryEntriesByUserId() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        // Create multiple diary entries for the same user
        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(new DiaryEntry("content1", LocalDateTime.now(), loggedInUser, testMood, null));
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(new DiaryEntry("content2", LocalDateTime.now().plusDays(1), loggedInUser, testMood, null));
        DiaryEntry entry3 = diaryEntryService.createDiaryEntry(new DiaryEntry("content3", LocalDateTime.now().plusDays(2), loggedInUser, testMood, null));
        
        java.util.List<DiaryEntry> userEntries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId());
        
        assertEquals(3, userEntries.size());

    }

    @Test
    public void testGetDiaryEntriesByUserIdWithNoEntries() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        java.util.List<DiaryEntry> userEntries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId());
        
        assertEquals(0, userEntries.size());
    }

    @Test
    public void testCreateDiaryEntryWithNullContent() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");

        assertThrows(DataIntegrityViolationException.class, () -> 
            diaryEntryService.createDiaryEntry(new DiaryEntry(null, LocalDateTime.now(), loggedInUser, testMood, null)));
    }

    @Test
    public void testCreateDiaryEntryWithNullDate() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry entry = new DiaryEntry("content", null, loggedInUser, testMood, null);
        assertThrows(DataIntegrityViolationException.class, () -> diaryEntryService.createDiaryEntry(entry));
    }

    @Test
    public void testCreateMultipleDiaryEntriesForSameUser() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        diaryEntryService.createDiaryEntry(new DiaryEntry("content1", LocalDateTime.now(), loggedInUser, testMood, null));
        assertThrows(DuplicatedEntryException.class, () -> diaryEntryService.createDiaryEntry(new DiaryEntry( "content2", LocalDateTime.now(), loggedInUser, testMood, null)));
    }

    @Test
    public void testDiaryEntryPersistence() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime testDate = LocalDateTime.now();
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(new DiaryEntry("Test Content", testDate, loggedInUser, testMood, null));
        
        DiaryEntry foundEntry = diaryEntryService.getDiaryEntryById(entry.getId());
        assertEquals("Test Content", foundEntry.getContent());
        assertEquals(loggedInUser.getId(), foundEntry.getUser().getId());
        assertEquals(testMood.getId(), foundEntry.getMood().getId());
    }

    @Test
    public void testCreateEntriesDifferentDates() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(new DiaryEntry("Content 1", LocalDateTime.now(), loggedInUser, testMood, null));
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(new DiaryEntry("Content 2", LocalDateTime.now().plusDays(1), loggedInUser, testMood, null));
        DiaryEntry entry3 = diaryEntryService.createDiaryEntry(new DiaryEntry("Content 3", LocalDateTime.now().plusDays(2), loggedInUser, testMood, null));
        
        assertNotNull(entry1.getId());
        assertNotNull(entry2.getId());
        assertNotNull(entry3.getId());
        assertNotEquals(entry1.getId(), entry2.getId());
        assertNotEquals(entry2.getId(), entry3.getId());
    }

    @Test
    public void testCreateEntryAfterDeletingPrevious() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime sameDate = LocalDateTime.now();
        
        DiaryEntry firstEntry = diaryEntryService.createDiaryEntry(new DiaryEntry("Content 1", sameDate, loggedInUser, testMood, null));
        
        diaryEntryService.deleteDiaryEntry(firstEntry);
        
        DiaryEntry newEntry = diaryEntryService.createDiaryEntry(new DiaryEntry("Content 2", sameDate, loggedInUser, testMood, null));
        assertNotNull(newEntry.getId());
    }

    @Test
    public void testCreateEntryWithVeryLongContent() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        String longContent = "This is a very long content that should be stored as TEXT in the database. ".repeat(100);
        
        assertThrows(DataIntegrityViolationException.class, () -> diaryEntryService.createDiaryEntry(new DiaryEntry(longContent, LocalDateTime.now(), loggedInUser, testMood, null)));
       
    }

    @Test
    public void testCreateEntryWithSpecialCharacters() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        String specialContent = "Contenido con símbolos: @#$%^&*()_+-=[]{}|;':\",./<>?";
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(new DiaryEntry(specialContent, LocalDateTime.now(), loggedInUser, testMood, null));

        assertEquals(specialContent, entry.getContent());
    }


    @Test
    public void testMultipleUsersCanCreateEntriesSameDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users user2 = new Users("maria", "5678", "María", "López", "maria@gmail.com");
        userService.signUp(user2);
        Users loggedInUser1 = userService.login("pablo", "1234");
        Users loggedInUser2 = userService.login("maria", "5678");
        
        LocalDateTime sameDate = LocalDateTime.now();
        
        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(new DiaryEntry("Content 1", sameDate, loggedInUser1, testMood, null));
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(new DiaryEntry("Content 2", sameDate, loggedInUser2, testMood, null));
        
        assertNotNull(entry1.getId());
        assertNotNull(entry2.getId());
        assertNotEquals(entry1.getId(), entry2.getId());
    }


    @Test
    public void testCreateEntryWithPastDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime pastDate = LocalDateTime.now().minusDays(5);
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(new DiaryEntry( "Content", pastDate, loggedInUser, testMood, null));
        assertEquals(pastDate.toLocalDate(), entry.getDate().toLocalDate());
    }

    @Test
    public void testCreateEntryWithFutureDate() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime futureDate = LocalDateTime.now().plusDays(5);
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(new DiaryEntry("Content", futureDate, loggedInUser, testMood, null));
        assertEquals(futureDate.toLocalDate(), entry.getDate().toLocalDate());
    }


    @Test
    public void testUpdateEntryContent() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry entry = diaryEntryService.createDiaryEntry(new DiaryEntry("Original Content", LocalDateTime.now(), loggedInUser, testMood, null));
        
        entry.setContent("Updated Content");
        
        DiaryEntry updatedEntry = diaryEntryService.updateDiaryEntry(entry);
        assertEquals("Updated Content", updatedEntry.getContent());
    }

    @Test
    public void testUpdateEntryDate() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        LocalDateTime originalDate = LocalDateTime.now();
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        
        DiaryEntry entry = diaryEntryService.createDiaryEntry(new DiaryEntry("Content", originalDate, loggedInUser, testMood, null));
        
        entry.setDate(newDate);
        DiaryEntry updatedEntry = diaryEntryService.updateDiaryEntry(entry);
        assertEquals(newDate.toLocalDate(), updatedEntry.getDate().toLocalDate());
    }


    @Test
    public void testGetAllEntriesForUser() throws DuplicateInstanceException, IncorrectLoginException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        diaryEntryService.createDiaryEntry(new DiaryEntry("Content 1", LocalDateTime.now(), loggedInUser, testMood, null));
        diaryEntryService.createDiaryEntry(new DiaryEntry("Content 2", LocalDateTime.now().plusDays(1), loggedInUser, testMood, null));
        diaryEntryService.createDiaryEntry(new DiaryEntry("Content 3", LocalDateTime.now().plusDays(2), loggedInUser, testMood, null));
        
        java.util.List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId());
        assertEquals(3, entries.size());
    }

    @Test
    public void testDeleteAllEntriesForUser() throws DuplicateInstanceException, IncorrectLoginException, InstanceNotFoundException, DuplicatedEntryException {
        Users loggedInUser = userService.login("pablo", "1234");
        
        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(new DiaryEntry( "Content 1", LocalDateTime.now(), loggedInUser, testMood, null));
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(new DiaryEntry("Content 2", LocalDateTime.now().plusDays(1), loggedInUser, testMood, null));
        
        diaryEntryService.deleteDiaryEntry(entry1);
        diaryEntryService.deleteDiaryEntry(entry2);
        
        java.util.List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId());
        assertEquals(0, entries.size());
    }


    @Test
    public void testCreateEntryWithInvalidUserId() {
        Users invalidUser = new Users();
        invalidUser.setId(999L);
        
        assertThrows(DataIntegrityViolationException.class, () -> 
            diaryEntryService.createDiaryEntry(new DiaryEntry("Content", LocalDateTime.now(), invalidUser, testMood, null)));
    }

    @Test
    public void testCreateEntryWithInvalidMoodId() throws DuplicateInstanceException, IncorrectLoginException {
        Users loggedInUser = userService.login("pablo", "1234");
        Mood invalidMood = new Mood();
        invalidMood.setId(999L);
        
        assertThrows(DataIntegrityViolationException.class, () -> 
            diaryEntryService.createDiaryEntry(new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, invalidMood, null)));
    }

    @Test
    public void testUpdateDiaryEntryMood() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry entry = diaryEntryService.createDiaryEntry(
            new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, testMood, null)
        );
        Mood newMood = new Mood();
        newMood.setName("Sad");
        newMood.setImage(new byte[]{5, 6, 7, 8});
        moodDao.save(newMood);

        entry.setMood(newMood);
        DiaryEntry updated = diaryEntryService.updateDiaryEntry(entry);
        assertEquals(newMood.getId(), updated.getMood().getId());
    }

    @Test
    public void testDeleteDiaryEntryTwice() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry entry = diaryEntryService.createDiaryEntry(
            new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, testMood, null)
        );
        diaryEntryService.deleteDiaryEntry(entry);
        assertThrows(InstanceNotFoundException.class, () -> diaryEntryService.deleteDiaryEntry(entry));
    }

    @Test
    public void testGetDiaryEntriesByUserIdWithGaps() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        diaryEntryService.createDiaryEntry(new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, testMood, null));
        diaryEntryService.createDiaryEntry(new DiaryEntry( "Content", LocalDateTime.now().plusDays(9), loggedInUser, testMood, null));
        java.util.List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesByUserId(loggedInUser.getId());
        assertEquals(2, entries.size());
    }

    @Test
    public void testCreateDiaryEntryWithDifferentMoods() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        DiaryEntry entry1 = diaryEntryService.createDiaryEntry(
            new DiaryEntry( "Content1", LocalDateTime.now(), loggedInUser, testMood, null)
        );
        DiaryEntry entry2 = diaryEntryService.createDiaryEntry(
            new DiaryEntry("Content2", LocalDateTime.now().plusDays(1), loggedInUser, testMood2, null)
        );
        assertNotEquals(entry1.getMood().getId(), entry2.getMood().getId());
    }

    @Test
    public void testCreateDiaryEntryWithMultipleImages() throws Exception {
        Users loggedInUser = userService.login("pablo", "1234");
        Images img1 = new Images();
        img1.setImageData(new byte[]{1,2,3});
        img1.setUploadDate(LocalDateTime.now());
        Images img2 = new Images();
        img2.setImageData(new byte[]{4,5,6});
        img2.setUploadDate(LocalDateTime.now());
        Set<Images> images = new HashSet<>();
        images.add(img1);
        images.add(img2);
        DiaryEntry entry = diaryEntryService.createDiaryEntry(
            new DiaryEntry("Content", LocalDateTime.now(), loggedInUser, testMood, images)
        );
        assertNotNull(entry.getImages());
        assertEquals(2, entry.getImages().size());
    }

}
