package com.tfg.tfg_app.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.services.WeeklySummaryService;
import com.tfg.tfg_app.rest.dtos.WeeklySummaryConversor;
import com.tfg.tfg_app.rest.dtos.WeeklySummaryDto;

@RestController
@RequestMapping("/api/weekly-summary")
public class WeeklySummaryController {

    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @GetMapping("")
    public Page<WeeklySummaryDto> getWeeklySummaryForUser(@RequestAttribute Long userId, @RequestParam int page, @RequestParam int size) throws InstanceNotFoundException {
        return WeeklySummaryConversor.toWeeklySummaryDtoPage(weeklySummaryService.getWeeklySummariesByUserId(userId, page, size));
    }
}
