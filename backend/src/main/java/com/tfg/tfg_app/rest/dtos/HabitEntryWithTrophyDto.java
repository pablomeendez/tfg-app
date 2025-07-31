package com.tfg.tfg_app.rest.dtos;

public class HabitEntryWithTrophyDto {
    
    private HabitEntryDto habitEntry;
    private UserTrophyDto userTrophy;

    public HabitEntryWithTrophyDto() {

    }

    public HabitEntryWithTrophyDto(HabitEntryDto habitEntry, UserTrophyDto userTrophy) {
        this.habitEntry = habitEntry;
        this.userTrophy = userTrophy;
    }

    public HabitEntryDto getHabitEntry() {
        return habitEntry;
    }

    public void setHabitEntry(HabitEntryDto habitEntry) {
        this.habitEntry = habitEntry;
    }

    public UserTrophyDto getUserTrophy() {
        return userTrophy;
    }

    public void setUserTrophy(UserTrophyDto userTrophy) {
        this.userTrophy = userTrophy;
    }
}
