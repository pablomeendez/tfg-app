package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;
import java.util.List;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Trophy;
import com.tfg.tfg_app.model.entities.UserTrophy;

public interface TrophyService {

    List<Trophy> getAllTrophies();

    UserTrophy checkAndAwardUserTrophy(Long userId, Long habitId, int days) throws InstanceNotFoundException;

    List<UserTrophy> getUserTrophies(Long userId) throws InstanceNotFoundException;

    List<UserTrophy> getUserTrophiesByUserIdAndHabitId(Long userId, Long habitId) throws InstanceNotFoundException;

    List<UserTrophy> getUserTrophiesByUserIdAndDate(Long userId, LocalDateTime date) throws InstanceNotFoundException;
}
