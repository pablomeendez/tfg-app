package com.tfg.tfg_app.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.services.HabitService;
import com.tfg.tfg_app.rest.dtos.HabitDto;
import com.tfg.tfg_app.rest.dtos.HabitEntryDto;
import com.tfg.tfg_app.rest.dtos.UserHabitDto;
import static com.tfg.tfg_app.rest.dtos.HabitConversor.*;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;
    
    @GetMapping("/all")
    List<HabitDto> getAllHabits(){
        return toHabitDtos(habitService.getAllHabits());
    }

    @PostMapping("/userHabit")
    UserHabitDto createUserHabit(@RequestAttribute Long userId, @RequestBody Long habitId) throws InstanceNotFoundException {
        return toUserHabitDto(habitService.createUserHabit(userId, habitId));
    }

    @GetMapping("/")
    List<UserHabitDto> getHabitsByUserId(@RequestParam Long userId) throws InstanceNotFoundException {
        return toUserHabitDtos(habitService.getHabitsByUserId(userId));
    }

    @PostMapping("/entry")
    HabitEntryDto createHabitEntry(@RequestAttribute Long userId, @RequestBody Long userHabitId) throws InstanceNotFoundException {
        return toHabitEntryDto(habitService.createHabitEntry(userId, userHabitId));
    }

}
