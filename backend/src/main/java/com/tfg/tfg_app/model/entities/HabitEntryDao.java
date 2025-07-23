package com.tfg.tfg_app.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitEntryDao extends JpaRepository<HabitEntry, Long> {

    HabitEntry findTopByUserIdOrderByIdDesc(Long userId);
} 