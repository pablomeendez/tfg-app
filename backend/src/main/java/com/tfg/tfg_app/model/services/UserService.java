package com.tfg.tfg_app.model.services;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.IncorrectLoginException;

public interface UserService {
    
    void signUp(Users user) throws DuplicateInstanceException;

    Users login(String userName, String password) throws IncorrectLoginException;

}
