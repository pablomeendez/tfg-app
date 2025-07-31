package com.tfg.tfg_app.rest.dtos;

public class HabitEntryParamsDto {
    private Long userHabitId;
    private Long diaryEntryId;

    public HabitEntryParamsDto(Long userHabitId, Long diaryEntryId) {
        this.userHabitId = userHabitId;
        this.diaryEntryId = diaryEntryId;
    }

    public Long getUserHabitId() {
        return userHabitId;
    }

    public Long getDiaryEntryId() {
        return diaryEntryId;
    }
}
