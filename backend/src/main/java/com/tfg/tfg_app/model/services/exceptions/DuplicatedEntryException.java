package com.tfg.tfg_app.model.services.exceptions;

import java.time.LocalDate;

@SuppressWarnings("serial")
public class DuplicatedEntryException extends Exception {
    
    private final String userName;

    private final LocalDate date;

    public DuplicatedEntryException(String userName, LocalDate date) {
        this.userName = userName;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDate getDate() {
        return date;
    }

}
