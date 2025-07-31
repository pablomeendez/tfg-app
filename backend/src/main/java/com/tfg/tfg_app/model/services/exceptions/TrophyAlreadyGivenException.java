package com.tfg.tfg_app.model.services.exceptions;

public class TrophyAlreadyGivenException extends Exception {

    private Long userId;
    private Long trophyId;

    public TrophyAlreadyGivenException(Long userId, Long trophyId) {
        super("Trophy with ID " + trophyId + " has already been given to user with ID " + userId);
        this.userId = userId;
        this.trophyId = trophyId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTrophyId() {
        return trophyId;
    }
}
