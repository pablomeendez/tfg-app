package com.tfg.tfg_app.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.UserTrophy;
import com.tfg.tfg_app.model.services.HabitService;
import com.tfg.tfg_app.model.services.TrophyService;
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;
import com.tfg.tfg_app.rest.dtos.HabitDto;
import com.tfg.tfg_app.rest.dtos.HabitEntryDto;
import com.tfg.tfg_app.rest.dtos.HabitEntryParamsDto;
import com.tfg.tfg_app.rest.dtos.HabitEntryWithTrophyDto;
import com.tfg.tfg_app.rest.dtos.TrophyConversor;
import com.tfg.tfg_app.rest.dtos.UserHabitDto;
import static com.tfg.tfg_app.rest.dtos.HabitConversor.*;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @Autowired
    private TrophyService trophyService;
    
    @GetMapping("/all")
    List<HabitDto> getAllHabits(){
        return toHabitDtos(habitService.getAllHabits());
    }

    @PostMapping("/userHabit")
    UserHabitDto createUserHabit(@RequestAttribute Long userId, @RequestBody Long habitId) throws InstanceNotFoundException {
        return toUserHabitDto(habitService.createUserHabit(userId, habitId));
    }

    @DeleteMapping("/userHabit/{id}")
    void deleteUserHabit(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException {
        habitService.deleteUserHabit(id);
    }

    @GetMapping("/")
    List<UserHabitDto> getHabitsByUserId(@RequestParam Long userId) throws InstanceNotFoundException {
        return toUserHabitDtos(habitService.getHabitsByUserId(userId));
    }

    @PostMapping("/entry")
    HabitEntryWithTrophyDto createHabitEntry(@RequestAttribute Long userId, @RequestBody HabitEntryParamsDto params) throws InstanceNotFoundException, TrophyAlreadyGivenException {
        HabitEntry habitEntry = habitService.createHabitEntry(userId, params.getUserHabitId(), params.getDiaryEntryId());
        UserTrophy userTrophy = trophyService.giveUserTrophy(userId, habitEntry.getUserHabit().getHabit().getId(), habitEntry.getStreak());
        return toHabitEntryWithTrophyDto(habitEntry, userTrophy);
    }

    @DeleteMapping("/entry/{habitEntryId}")
    HabitEntryDto deleteHabitEntry(@RequestAttribute Long userId, @PathVariable Long habitEntryId) throws InstanceNotFoundException {
        return toHabitEntryDto(habitService.deleteHabitEntry(userId, habitEntryId));
    }

    @GetMapping("/entries")
    List<HabitEntryDto> getHabitEntriesByUserIdAndHabitId(@RequestAttribute Long userId, @RequestParam Long userHabitId) throws InstanceNotFoundException {
        return toHabitEntryDtos(habitService.getHabitEntriesByUserIdAndUserHabitId(userId, userHabitId));
    }

}
