package com.tfg.tfg_app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.tfg_app.model.entities.DiaryEntry;

@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
} 