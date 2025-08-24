package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;

public interface DiaryEntryService {

    DiaryEntry createDiaryEntry(Long userId, DiaryEntry diaryEntry, List<byte[]> images, List<UserHabit> habits) throws DuplicateInstanceException, DuplicatedEntryException, InstanceNotFoundException, TrophyAlreadyGivenException;
    
    DiaryEntry getDiaryEntryById(Long id) throws InstanceNotFoundException;

    Page<DiaryEntry> getDiaryEntriesByUserId(Long userId, int page, int size);

    List<Mood> getAllMoods();

    DiaryEntry getMostFrequentMood(Long userId, LocalDateTime date);

    List<DiaryEntry> getDiaryEntriesByUserIdAndDate(Long userId, LocalDateTime date);

    DiaryEntry getLatestDiaryEntry(Long userId) throws InstanceNotFoundException;
}
