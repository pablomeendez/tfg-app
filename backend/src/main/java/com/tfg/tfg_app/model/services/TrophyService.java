package com.tfg.tfg_app.model.services;

import java.util.List;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Trophy;
import com.tfg.tfg_app.model.entities.UserTrophy;
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;

public interface TrophyService {

    List<Trophy> getAllTrophies();

    List<Trophy> getTrophiesByUserId(Long userId);

    Trophy getTrophyById(Long trophyId) throws InstanceNotFoundException;

    UserTrophy giveUserTrophy(Long userId, Long habitId, int days) throws InstanceNotFoundException, TrophyAlreadyGivenException;

    List<UserTrophy> getUserTrophies(Long userId) throws InstanceNotFoundException;

    List<UserTrophy> getUserTrophiesByUserIdAndHabitId(Long userId, Long habitId) throws InstanceNotFoundException;
}
