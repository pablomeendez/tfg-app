package com.tfg.tfg_app.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitDao extends JpaRepository<Habit, Long> {
} 