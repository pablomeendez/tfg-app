package com.tfg.tfg_app.model.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.DiaryEntryDao;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.MoodDao;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;

@Service
public class DiaryEntryServiceImpl implements DiaryEntryService {

    @Autowired
    private DiaryEntryDao diaryEntryDao;

    @Autowired
    private MoodDao moodDao;

    @Override
    public DiaryEntry createDiaryEntry(DiaryEntry diaryEntry) throws DuplicateInstanceException, DuplicatedEntryException {

        if (diaryEntry.getUser() == null) {
            throw new DataIntegrityViolationException(null);
        }
        List<DiaryEntry> diaryEntries = diaryEntryDao.findByUserId(diaryEntry.getUser().getId());

        if (diaryEntry.getDate() == null) {
            diaryEntry.setDate(LocalDateTime.now());
        }

        if (diaryEntries.size() > 0 && diaryEntries.get(diaryEntries.size() - 1).getDate().toLocalDate().isEqual(diaryEntry.getDate().toLocalDate())) {
            throw new DuplicatedEntryException(diaryEntry.getUser().toString(), LocalDate.now());
        }

        return diaryEntryDao.save(diaryEntry);
    }

    @Override
    public DiaryEntry updateDiaryEntry(DiaryEntry diaryEntry) throws InstanceNotFoundException {

        if (!diaryEntryDao.findById(diaryEntry.getId()).isPresent()) {
            throw new InstanceNotFoundException("Diary entry not found", diaryEntry);
        }

        return diaryEntryDao.save(diaryEntry);
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
    public List<DiaryEntry> getDiaryEntriesByUserId(Long userId) {
        return diaryEntryDao.findByUserId(userId);
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
