package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.UserHabit;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;

public interface DiaryEntryService {

    DiaryEntry createDiaryEntry(Long userId, DiaryEntry diaryEntry, List<byte[]> images, List<UserHabit> habits) throws DuplicatedEntryException, InstanceNotFoundException;

    DiaryEntry getDiaryEntryById(Long id) throws InstanceNotFoundException;

    Page<DiaryEntry> getDiaryEntriesByUserId(Long userId, int page, int size);

    List<Mood> getAllMoods();

    DiaryEntry getWeeksMostFrequentMood(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<DiaryEntry> getDiaryEntriesByUserIdAndDate(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    DiaryEntry getLatestDiaryEntry(Long userId) throws InstanceNotFoundException;
}
