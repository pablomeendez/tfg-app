package com.tfg.tfg_app.rest.dtos;

public class ChangeUserPasswordDto {

    private String oldPassword;
    private String newPassword;

    public ChangeUserPasswordDto() {
    }

    public ChangeUserPasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
}
