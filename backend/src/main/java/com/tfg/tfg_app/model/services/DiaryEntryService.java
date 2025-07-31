package com.tfg.tfg_app.model.services;

import java.util.List;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;

public interface DiaryEntryService {
    
    DiaryEntry createDiaryEntry(DiaryEntry diaryEntry) throws DuplicateInstanceException, DuplicatedEntryException;

    DiaryEntry updateDiaryEntry(DiaryEntry diaryEntry) throws InstanceNotFoundException;

    void deleteDiaryEntry(DiaryEntry diaryEntry) throws InstanceNotFoundException;

    DiaryEntry getDiaryEntryById(Long id) throws InstanceNotFoundException;

    List<DiaryEntry> getDiaryEntriesByUserId(Long userId);

    List<Mood> getAllMoods();
}
