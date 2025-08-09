package com.tfg.tfg_app.model.entities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklySummaryDao extends JpaRepository<WeeklySummary, Long> {

    Page<WeeklySummary> findByUserIdOrderByDateDesc(Long userId, Pageable pageable);
} 