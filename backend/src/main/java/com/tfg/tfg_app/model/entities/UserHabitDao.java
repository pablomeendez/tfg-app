package com.tfg.tfg_app.model.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHabitDao extends JpaRepository<UserHabit, Long> {

    List<UserHabit> findByUserId(Long userId);
} 