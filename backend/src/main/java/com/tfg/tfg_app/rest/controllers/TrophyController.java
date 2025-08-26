package com.tfg.tfg_app.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.services.TrophyService;
import com.tfg.tfg_app.rest.dtos.TrophyConversor;
import com.tfg.tfg_app.rest.dtos.TrophyDto;
import com.tfg.tfg_app.rest.dtos.UserTrophyDto;

@RestController
@RequestMapping("/api/trophies")
public class TrophyController {

    @Autowired
    private TrophyService trophyService;

    @GetMapping("")
    public List<TrophyDto> getAllTrophies() {
        return TrophyConversor.toTrophyDtos(trophyService.getAllTrophies());
    }

    @GetMapping("/user-trophy")
    public List<UserTrophyDto> getUserTrophiesByUser(@RequestAttribute Long userId) throws InstanceNotFoundException{
        return TrophyConversor.toUserTrophyDtos(trophyService.getUserTrophies(userId));
    }

    @GetMapping("/user-trophy/habit")
    public List<UserTrophyDto> getUserTrophiesByUserAndHabit(@RequestAttribute Long userId, @RequestParam Long habitId) throws InstanceNotFoundException {
        return TrophyConversor.toUserTrophyDtos(trophyService.getUserTrophiesByUserIdAndHabitId(userId, habitId));
    }

}   
