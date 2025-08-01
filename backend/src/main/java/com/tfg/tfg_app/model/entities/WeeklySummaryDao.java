package com.tfg.tfg_app.model.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklySummaryDao extends JpaRepository<WeeklySummary, Long> {

    List<WeeklySummary> findByUserId(Long userId);
} 