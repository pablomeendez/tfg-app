package com.tfg.tfg_app.model.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.DiaryEntryDao;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.Images;
import com.tfg.tfg_app.model.entities.ImagesDao;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.MoodDao;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;

@Service
public class DiaryEntryServiceImpl implements DiaryEntryService {

    @Autowired
    private DiaryEntryDao diaryEntryDao;

    @Autowired
    private UserService userService;

    @Autowired
    private HabitService habitService;

    @Autowired
    private TrophyService trophyService;

    @Autowired
    private MoodDao moodDao;

    @Autowired
    private ImagesDao imagesDao;

    @Override
    public DiaryEntry createDiaryEntry(Long userId, DiaryEntry diaryEntry, List<byte[]> images, List<UserHabit> habits) throws DuplicateInstanceException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException {

        Users user = userService.checkUser(userId);
        
        diaryEntry.setUser(user);

        List<DiaryEntry> diaryEntries = diaryEntryDao.findByUserIdOrderByDateDesc(userId, PageRequest.of(0, 1)).getContent();

        if (diaryEntry.getDate() == null) {
            diaryEntry.setDate(LocalDateTime.now());
        }

        if (diaryEntries.size() > 0 && diaryEntries.get(0).getDate().toLocalDate().isEqual(diaryEntry.getDate().toLocalDate())) {
            throw new DuplicatedEntryException(userId.toString(), LocalDate.now());
        }

        DiaryEntry createdDiaryEntry = diaryEntryDao.save(diaryEntry);

        List<HabitEntry> habitEntries = new ArrayList<>();

        habits.forEach(userHabit -> {
            try {
                HabitEntry habitEntryResult = habitService.createHabitEntry(userId, userHabit.getId(), createdDiaryEntry.getId());
                habitEntries.add(habitEntryResult);
                trophyService.checkAndAwardUserTrophy(userId, habitEntryResult.getHabit().getId(), habitEntryResult.getStreak());
            } catch (InstanceNotFoundException  e) {
                throw new RuntimeException("Error creating habit entry: " + e.getMessage(), e);
            } catch (TrophyAlreadyGivenException e) {
                throw new RuntimeException("Trophy already given: " + e.getMessage(), e)    ;
            }
        });

        Set<HabitEntry> habitEntriesSet = new HashSet<>(habitEntries);
        habitEntriesSet.addAll(habitEntries);

        createdDiaryEntry.setHabitEntries(habitEntriesSet);

        Set<Images> imagesSet = new HashSet<>();

        images.forEach(imageBytes -> {
            Images image = new Images(createdDiaryEntry, imageBytes);
            imagesSet.add(imagesDao.save(image));
        });

        createdDiaryEntry.setImages(imagesSet); 

        return createdDiaryEntry;
    }

    @Override
    public void deleteDiaryEntry(DiaryEntry diaryEntry) throws InstanceNotFoundException {

        if (!diaryEntryDao.findById(diaryEntry.getId()).isPresent()) {
            throw new InstanceNotFoundException("Diary entry not found", diaryEntry);
        }

        diaryEntryDao.delete(diaryEntry);
    }

    @Override
    public DiaryEntry getDiaryEntryById(Long id) throws InstanceNotFoundException {
        return diaryEntryDao.findById(id)
            .orElseThrow(() -> new InstanceNotFoundException("Diary entry not found", id));
    }

    @Override
    public Page<DiaryEntry> getDiaryEntriesByUserId(Long userId, int page, int size) {
        return diaryEntryDao.findByUserIdOrderByDateDesc(userId, PageRequest.of(page, size));
    }

    @Override
    public List<Mood> getAllMoods() {
        return moodDao.findAll();
    }

    @Override
    public DiaryEntry getMostFrequentMood(Long userId, LocalDateTime date) {
        return diaryEntryDao.findMostFrequentMood(userId, date.minusDays(7), date);
    }

    @Override
    public List<DiaryEntry> getDiaryEntriesByUserIdAndDate(Long userId, LocalDateTime date) {
        return diaryEntryDao.findByUserIdAndDateBetween(userId, date.minusDays(7), date);
    }

    @Override
    public DiaryEntry getLatestDiaryEntry(Long userId) throws InstanceNotFoundException {
        return diaryEntryDao.findFirstByUserIdOrderByDateDesc(userId);
    }
}
