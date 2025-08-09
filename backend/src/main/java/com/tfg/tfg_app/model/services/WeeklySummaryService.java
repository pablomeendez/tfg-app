package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.WeeklySummary;

public interface WeeklySummaryService {

    WeeklySummary generateWeeklySummary(Long userId, LocalDateTime date) throws InstanceNotFoundException;

    Page<WeeklySummary> getWeeklySummariesByUserId(Long userId, int page, int size) throws InstanceNotFoundException;

    WeeklySummary getWeeklySummaryById(Long summaryId) throws InstanceNotFoundException;
}
