package com.tfg.tfg_app.model.services;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.IncorrectLoginException;
import com.tfg.tfg_app.model.services.exceptions.IncorrectPasswordException;

public interface UserService {
    
    void signUp(Users user) throws DuplicateInstanceException;

    Users login(String userName, String password) throws IncorrectLoginException;

    Users loginFromId(Long id) throws InstanceNotFoundException;

    Users updateProfile(Long id, String firstName, String lastName, String email)
        throws InstanceNotFoundException;

    void changePassword(Long id, String oldPassword, String newPassword) 
        throws InstanceNotFoundException, IncorrectPasswordException;

}
