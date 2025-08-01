package com.tfg.tfg_app.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.services.WeeklySummaryService;
import com.tfg.tfg_app.rest.dtos.WeeklySummaryConversor;
import com.tfg.tfg_app.rest.dtos.WeeklySummaryDto;

@RestController
@RequestMapping("/api/weeklySummary")
public class WeeklySummaryController {

    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @GetMapping("/user")
    public List<WeeklySummaryDto> getWeeklySummaryForUser(@RequestParam Long userId) throws InstanceNotFoundException {
        return WeeklySummaryConversor.toWeeklySummaryDtos(weeklySummaryService.getWeeklySummariesByUserId(userId));
    }
}
