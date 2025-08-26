package com.tfg.tfg_app.model.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.entities.UsersDao;


@Service
@Transactional(readOnly=true)
public class PermissionCheckerImpl implements PermissionChecker {

	@Autowired
	private UsersDao userDao;

	@Override
	public Users checkUser(Long userId) throws InstanceNotFoundException {

		Optional<Users> user = userDao.findById(userId);
		
		if (!user.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		
		return user.get();
		
	}

}

