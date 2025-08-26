package com.tfg.tfg_app.model.services;

import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Users;

public interface PermissionChecker {
	
	/**
	 * Check user.
	 *
	 * @param userId the user id
	 * @return the user
	 * @throws InstanceNotFoundException the instance not found exception
	 */
	public Users checkUser(Long userId) throws InstanceNotFoundException;
}
