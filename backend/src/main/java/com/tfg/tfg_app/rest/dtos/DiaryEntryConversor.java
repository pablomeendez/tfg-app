package com.tfg.tfg_app.rest.dtos;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.Images;
import com.tfg.tfg_app.model.entities.Mood;

public class DiaryEntryConversor {

    private DiaryEntryConversor() {}
    
    public static DiaryEntryDto toDiaryEntryDto (DiaryEntry diaryEntry) {
        return new DiaryEntryDto(diaryEntry.getContent(), diaryEntry.getImages().stream().map(Images::getImageData).collect(Collectors.toList()), diaryEntry.getMood().getId());
    }

    public static MoodDto toMoodDto (Mood mood) {
        return new MoodDto(mood.getId(), mood.getName(), mood.getImage());
    }

    public static DiaryEntry toDiaryEntry (DiaryEntryDto diaryEntryDto) {
        return new DiaryEntry(diaryEntryDto.getDescription(), null, null, null, null);
    }

    public static DiaryEntryResponseDto toDiaryEntryResponseDto(DiaryEntry diaryEntry) {
        return new DiaryEntryResponseDto(diaryEntry.getId(), diaryEntry.getContent(), diaryEntry.getDate(), UserConversor.toUserDto(diaryEntry.getUser()), toImageDtos(diaryEntry.getImages()), diaryEntry.getMood().getId());
    }

    public static List<DiaryEntryResponseDto> toDiaryEntryResponsesDto(List<DiaryEntry> diaryEntries) {
        return diaryEntries.stream().map(DiaryEntryConversor::toDiaryEntryResponseDto).collect(Collectors.toList());
    }

    public static List<ImageDto> toImageDtos(Set<Images> images) {
        return images.stream().map(image -> new ImageDto(image.getId(), image.getImageData(), image.getUploadDate())).collect(Collectors.toList());
    }

    public static List<MoodDto> toMoodDtos(List<Mood> moods) {
        return moods.stream().map(DiaryEntryConversor::toMoodDto).collect(Collectors.toList());
    }
}
