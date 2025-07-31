package com.tfg.tfg_app.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/")
    public List<TrophyDto> getAllTrophies() {
        return TrophyConversor.toTrophyDtos(trophyService.getAllTrophies());
    }

    @GetMapping("/user")
    public List<TrophyDto> getTrophiesByUserId(@RequestParam Long userId) {
        return TrophyConversor.toTrophyDtos(trophyService.getTrophiesByUserId(userId));
    }

    @GetMapping("/userTrophy")
    public List<UserTrophyDto> getUserTrophiesByUser(@RequestParam Long userId) throws InstanceNotFoundException{
        return TrophyConversor.toUserTrophyDtos(trophyService.getUserTrophies(userId));
    }

    @GetMapping("/userTrophy/habit")
    public List<UserTrophyDto> getUserTrophiesByUserAndHabit(@RequestParam Long userId, @RequestParam Long habitId) throws InstanceNotFoundException {
        return TrophyConversor.toUserTrophyDtos(trophyService.getUserTrophiesByUserIdAndHabitId(userId, habitId));
    }

}   
