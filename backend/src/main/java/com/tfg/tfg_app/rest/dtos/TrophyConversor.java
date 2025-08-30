package com.tfg.tfg_app.rest.dtos;

import java.util.List;

import com.tfg.tfg_app.model.entities.Trophy;
import com.tfg.tfg_app.model.entities.UserTrophy;

public class TrophyConversor {
    
    public static TrophyDto toTrophyDto(Trophy trophy) {
        return new TrophyDto(trophy.getId(), trophy.getName(), trophy.getDescription(), trophy.getDays(), trophy.getImage());
    }

    public static Trophy toTrophy(TrophyDto trophyDto) {
        return new Trophy(trophyDto.getId(), trophyDto.getName(), trophyDto.getDescription(), trophyDto.getDays(), trophyDto.getImage());
    }

    public static List<TrophyDto> toTrophyDtos(List<Trophy> trophies) {
        return trophies.stream().map(TrophyConversor::toTrophyDto).toList();
    }

    public static UserTrophyDto toUserTrophyDto(UserTrophy userTrophy) {
        if (userTrophy == null) {
            return null;
        }
        return new UserTrophyDto(userTrophy.getId(), UserConversor.toUserDto(userTrophy.getUser()), TrophyConversor.toTrophyDto(userTrophy.getTrophy()), HabitConversor.toHabitDto(userTrophy.getHabit()), userTrophy.getObtainedAt());
    }

    public static List<UserTrophyDto> toUserTrophyDtos(List<UserTrophy> userTrophies) {
        return userTrophies.stream().map(TrophyConversor::toUserTrophyDto).toList();
    }

    
}
