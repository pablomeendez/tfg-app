package com.tfg.tfg_app.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.tfg.tfg_app.rest.dtos.DiaryEntryConversor.*;

import java.util.List;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.services.DiaryEntryService;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;
import com.tfg.tfg_app.rest.dtos.DiaryEntryDto;
import com.tfg.tfg_app.rest.dtos.DiaryEntryResponseDto;
import com.tfg.tfg_app.rest.dtos.HabitConversor;
import com.tfg.tfg_app.rest.dtos.MoodDto;

@RestController
@RequestMapping("/api/diaryEntry")
public class DiaryEntryController {
    
    @Autowired
    private DiaryEntryService diaryEntryService;

    @PostMapping("")
    public DiaryEntryResponseDto createDiaryEntry(
        @RequestAttribute Long userId,
        @RequestBody DiaryEntryDto diaryEntryDto
    ) throws DuplicateInstanceException, DuplicatedEntryException, InstanceNotFoundException {

        return toDiaryEntryResponseDto(diaryEntryService.createDiaryEntry(userId, toDiaryEntry(diaryEntryDto), diaryEntryDto.getImages(), HabitConversor.toUserHabits(diaryEntryDto.getHabits())));
    }

    @DeleteMapping("/")
    public void deleteDiaryEntry(@RequestParam Long diaryEntryId) throws InstanceNotFoundException {

        DiaryEntry diaryEntry = diaryEntryService.getDiaryEntryById(diaryEntryId);

        if (diaryEntry == null) {
            throw new InstanceNotFoundException("diaryEntry:", diaryEntryId);
        }
    
        diaryEntryService.deleteDiaryEntry(diaryEntry);
        
    }

    @GetMapping("/")
    public Page<DiaryEntryResponseDto> getDiaryEntriesByUser (@RequestAttribute Long userId, @RequestParam int page, @RequestParam int size) {

        return toDiaryEntryResponsesDto(diaryEntryService.getDiaryEntriesByUserId(userId, page, size));
    }

    @GetMapping("/moods")
    public List<MoodDto> getAllMoods() {
        return toMoodDtos(diaryEntryService.getAllMoods());
    }

    @GetMapping("/latest")
    public DiaryEntryResponseDto getLatestDiaryEntry(@RequestAttribute Long userId) throws InstanceNotFoundException {
        DiaryEntry latestEntry = diaryEntryService.getLatestDiaryEntry(userId);
        if (latestEntry == null) {
            return null;
        }
        return toDiaryEntryResponseDto(latestEntry);
    }
}
