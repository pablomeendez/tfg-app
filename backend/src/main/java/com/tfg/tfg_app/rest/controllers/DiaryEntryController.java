package com.tfg.tfg_app.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.tfg.tfg_app.rest.dtos.DiaryEntryConversor.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.DiaryEntry;
import com.tfg.tfg_app.model.entities.Images;
import com.tfg.tfg_app.model.entities.ImagesDao;
import com.tfg.tfg_app.model.entities.MoodDao;
import com.tfg.tfg_app.model.entities.UsersDao;
import com.tfg.tfg_app.model.services.DiaryEntryService;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;
import com.tfg.tfg_app.rest.dtos.DiaryEntryDto;
import com.tfg.tfg_app.rest.dtos.DiaryEntryResponseDto;


import com.tfg.tfg_app.model.entities.Mood;
import com.tfg.tfg_app.model.entities.Users;

@RestController
@RequestMapping("/api/diaryEntry")
public class DiaryEntryController {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private MoodDao moodDao;

    @Autowired
    private ImagesDao imagesDao;
    
    @Autowired
    private DiaryEntryService diaryEntryService;

    @PostMapping("/create")
    public DiaryEntryResponseDto createDiaryEntry(
        @RequestAttribute Long userId,
        @RequestBody DiaryEntryDto diaryEntryDto
    ) throws DuplicateInstanceException, DuplicatedEntryException, InstanceNotFoundException {

        DiaryEntry diaryEntry = toDiaryEntry(diaryEntryDto);

        Users user = usersDao.findById(userId)
            .orElseThrow(() -> new InstanceNotFoundException("User not found with id: " + userId, userId));
        diaryEntry.setUser(user);

        Mood mood = moodDao.findById(diaryEntryDto.getMoodId())
            .orElseThrow(() -> new DuplicateInstanceException("Mood not found with id: " + diaryEntryDto.getMoodId(), diaryEntryDto.getMoodId()));
        diaryEntry.setMood(mood);

        DiaryEntry createdDiaryEntry = diaryEntryService.createDiaryEntry(diaryEntry);

        Set<Images> images = new HashSet<>();

        diaryEntryDto.getImages().forEach(imageBytes -> {
                Images image = new Images(imageBytes);
                image.setDiaryEntry(createdDiaryEntry);
                images.add(imagesDao.save(image));
        });

        createdDiaryEntry.setImages(images);

        return toDiaryEntryResponseDto(createdDiaryEntry);
    }

    @PutMapping("/update/{diaryEntryId}")
    public DiaryEntryResponseDto updateDiaryEntry(@RequestAttribute Long userId, @PathVariable Long diaryEntryId ,@RequestBody DiaryEntryDto diaryEntryDto) throws InstanceNotFoundException, DuplicateInstanceException {


        if (diaryEntryService.getDiaryEntryById(diaryEntryId) == null) {
            throw new InstanceNotFoundException("diaryEntry", diaryEntryDto);
        }

        DiaryEntry diaryEntry = toDiaryEntry(diaryEntryDto);
        diaryEntry.setId(diaryEntryId);

        Users user = usersDao.findById(userId)
            .orElseThrow(() -> new InstanceNotFoundException("User not found with id: " + userId, userId));
        diaryEntry.setUser(user);

        Mood mood = moodDao.findById(diaryEntryDto.getMoodId())
            .orElseThrow(() -> new DuplicateInstanceException("Mood not found with id: " + diaryEntryDto.getMoodId(), diaryEntryDto.getMoodId()));
        diaryEntry.setMood(mood);


        return toDiaryEntryResponseDto(diaryEntryService.updateDiaryEntry(diaryEntry));
        
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
    public List<DiaryEntryResponseDto> getDiaryEntriesByUser (@RequestParam Long userId) {

        return toDiaryEntryResponsesDto(diaryEntryService.getDiaryEntriesByUserId(userId));
    }

}
