package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.HabitEntry;
import com.tfg.tfg_app.model.entities.HabitEntryDao;
import com.tfg.tfg_app.model.entities.Trophy;
import com.tfg.tfg_app.model.entities.TrophyDao;
import com.tfg.tfg_app.model.entities.UserTrophy;
import com.tfg.tfg_app.model.entities.UserTrophyDao;
import com.tfg.tfg_app.model.entities.Users;

@Service
public class TrophyServiceImpl implements TrophyService {

    @Autowired
    private TrophyDao trophyDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTrophyDao userTrophyDao;

    @Autowired
    private HabitEntryDao habitEntryDao;

    @Override
    public List<Trophy> getAllTrophies() {
        return trophyDao.findAll();
    }

    @Override
    public UserTrophy checkAndAwardUserTrophy(Long userId, Long habitEntryId, int days) throws InstanceNotFoundException {

        Users user = userService.checkUser(userId);

        Optional<HabitEntry> optHabitEntry = habitEntryDao.findById(habitEntryId);

        if (!optHabitEntry.isPresent()) {
            throw new InstanceNotFoundException("HabitEntry with ID " + habitEntryId + " not found.", HabitEntry.class);
        }

        HabitEntry habitEntry = optHabitEntry.get();

        if (days <= 0) {
            throw new IllegalArgumentException("Days must be greater than zero.");
        }

        Trophy trophy = trophyDao.findFirstByDays(days);

        if (trophy == null) {
            return null; 
        }

        List<UserTrophy> existingTrophies = userTrophyDao.findByUserIdAndHabitId(userId, habitEntry.getHabit().getId());

        if (!existingTrophies.isEmpty() && existingTrophies.stream().anyMatch(ut -> ut.getTrophy().getDays() == days)) {
            return null;
        }

        UserTrophy userTrophy = new UserTrophy(user, trophy, habitEntry.getHabit(), habitEntry, LocalDateTime.now());

        return userTrophyDao.save(userTrophy);
    }

    @Override
    public List<UserTrophy> getUserTrophies(Long userId) throws InstanceNotFoundException {
        userService.checkUser(userId);
        return userTrophyDao.findByUserId(userId);
    }

    @Override
    public List<UserTrophy> getUserTrophiesByUserIdAndHabitId(Long userId, Long habitId) throws InstanceNotFoundException {
        userService.checkUser(userId);
        return userTrophyDao.findByUserIdAndHabitId(userId, habitId);
    }

    @Override
    public List<UserTrophy> getUserTrophiesByUserIdAndDate(Long userId, LocalDateTime date) throws InstanceNotFoundException {
        userService.checkUser(userId);
        return userTrophyDao.findByUserIdAndObtainedAtBetween(userId, date.minusDays(7), date);
    }
}
