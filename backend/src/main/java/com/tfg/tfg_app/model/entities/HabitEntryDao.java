package com.tfg.tfg_app.model.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitEntryDao extends JpaRepository<HabitEntry, Long> {

    HabitEntry findTopByUserIdOrderByIdDesc(Long userId);

    List<HabitEntry> findByUserIdAndUserHabitId(Long userId, Long userHabitId);
} 