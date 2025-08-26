package com.tfg.tfg_app.model.services;

import java.util.List;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.IncorrectLoginException;

public interface UserService {
    
    void signUp(Users user) throws DuplicateInstanceException;

    Users login(String userName, String password) throws IncorrectLoginException;

    Users checkUser(Long id) throws InstanceNotFoundException;

    Users updateProfile(Long id, String firstName, String lastName, String email, Boolean firstEntry)
        throws InstanceNotFoundException;

    List<Users> getAllUsers() throws InstanceNotFoundException;

}
