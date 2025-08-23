package com.tfg.tfg_app.model.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Habit;
import com.tfg.tfg_app.model.entities.HabitDao;
import com.tfg.tfg_app.model.entities.Trophy;
import com.tfg.tfg_app.model.entities.TrophyDao;
import com.tfg.tfg_app.model.entities.UserTrophy;
import com.tfg.tfg_app.model.entities.UserTrophyDao;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;

@Service
public class TrophyServiceImpl implements TrophyService {

    @Autowired
    private TrophyDao trophyDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTrophyDao userTrophyDao;

    @Autowired
    private HabitDao habitDao;

    @Override
    public List<Trophy> getAllTrophies() {
        return trophyDao.findAll();
    }

    @Override
    public List<Trophy> getTrophiesByUserId(Long userId) {
        List<UserTrophy> userTrophies = userTrophyDao.findByUserId(userId);
        return userTrophies.stream()
                .map(UserTrophy::getTrophy)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Trophy getTrophyById(Long trophyId) throws InstanceNotFoundException {
        return trophyDao.findById(trophyId)
                .orElseThrow(() -> new InstanceNotFoundException(
                        "Trophy with ID " + trophyId + " not found.", Trophy.class));
    }

    @Override
    public UserTrophy checkAndAwardUserTrophy(Long userId, Long habitId, int days) throws InstanceNotFoundException, TrophyAlreadyGivenException {

        Users user = userService.checkUser(userId);
        if (user == null) {
            throw new InstanceNotFoundException("User with ID " + userId + " not found.", Users.class);
        }

        Optional<Habit> optHabit = habitDao.findById(habitId);

        if (!optHabit.isPresent()) {
            throw new InstanceNotFoundException("Habit with ID " + habitId + " not found.", Habit.class);
        }

        Habit habit = optHabit.get();

        if (days <= 0) {
            throw new IllegalArgumentException("Days must be greater than zero.");
        }

        Trophy trophy = trophyDao.findFirstByDays(days);

        if (trophy == null) {
            return null; 
        }

        List<UserTrophy> existingTrophies = userTrophyDao.findByUserIdAndHabitId(userId, habitId);

        if (!existingTrophies.isEmpty() && existingTrophies.stream().anyMatch(ut -> ut.getTrophy().getDays() == days)) {
            return null;
        }

        UserTrophy userTrophy = new UserTrophy(user, trophy, habit, LocalDateTime.now());

        return userTrophyDao.save(userTrophy);
    }

    @Override
    public List<UserTrophy> getUserTrophies(Long userId) throws InstanceNotFoundException {
        Users user = userService.checkUser(userId);
        if (user == null) {
            throw new InstanceNotFoundException("User with ID " + userId + " not found.", Users.class);
        }
        return userTrophyDao.findByUserId(userId);
    }

    @Override
    public List<UserTrophy> getUserTrophiesByUserIdAndHabitId(Long userId, Long habitId) throws InstanceNotFoundException {
        Users user = userService.checkUser(userId);
        if (user == null) {
            throw new InstanceNotFoundException("User with ID " + userId + " not found.", Users.class);
        }
        return userTrophyDao.findByUserIdAndHabitId(userId, habitId);
    }

    @Override
    public List<UserTrophy> getUserTrophiesByUserIdAndDate(Long userId, LocalDateTime date) throws InstanceNotFoundException {
        return userTrophyDao.findByUserIdAndObtainedAtBetween(userId, date.minusDays(7), date);
    }
}
