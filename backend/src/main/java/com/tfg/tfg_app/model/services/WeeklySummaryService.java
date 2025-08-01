package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;
import java.util.List;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.WeeklySummary;

public interface WeeklySummaryService {

    WeeklySummary generateWeeklySummary(Long userId, LocalDateTime date) throws InstanceNotFoundException;

    List<WeeklySummary> getWeeklySummariesByUserId(Long userId) throws InstanceNotFoundException;

    WeeklySummary getWeeklySummaryById(Long summaryId) throws InstanceNotFoundException;
}
