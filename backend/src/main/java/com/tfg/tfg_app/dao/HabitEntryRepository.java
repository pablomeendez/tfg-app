package com.tfg.tfg_app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.tfg_app.model.entities.HabitEntry;

@Repository
public interface HabitEntryRepository extends JpaRepository<HabitEntry, Long> {
} 