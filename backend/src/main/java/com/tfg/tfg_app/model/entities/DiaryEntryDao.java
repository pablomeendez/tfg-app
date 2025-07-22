package com.tfg.tfg_app.model.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryEntryDao extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByUserId(Long userId);
} 