package com.tfg.tfg_app.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import com.tfg.tfg_app.model.entities.DiaryEntry;

public class DiaryEntryConversor {

    private DiaryEntryConversor() {}
    
    public static DiaryEntryDto toDiaryEntryDto (DiaryEntry diaryEntry) {
        return new DiaryEntryDto(diaryEntry.getContent(), diaryEntry.getDate(), diaryEntry.getImages(), diaryEntry.getMood().getId());
    }

    public static DiaryEntry toDiaryEntry (DiaryEntryDto diaryEntryDto) {
        return new DiaryEntry(diaryEntryDto.getDescription(), diaryEntryDto.getDate(),null, null, diaryEntryDto.getImages()); 
    }

    public static DiaryEntryResponseDto toDiaryEntryResponseDto(DiaryEntry diaryEntry) {
        return new DiaryEntryResponseDto(diaryEntry.getId(), diaryEntry.getContent(), diaryEntry.getDate(), diaryEntry.getUser().getId(), diaryEntry.getMood().getId());
    }

    public static List<DiaryEntryResponseDto> toDiaryEntryResponsesDto(List<DiaryEntry> diaryEntries) {
        return diaryEntries.stream().map(DiaryEntryConversor::toDiaryEntryResponseDto).collect(Collectors.toList());
    }
}
