package com.tfg.tfg_app.model.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTrophyDao extends JpaRepository<UserTrophy, Long> {

    List<UserTrophy> findByUserId(Long userId);

    List<UserTrophy> findByUserIdAndHabitId(Long userId, Long habitId);
} 