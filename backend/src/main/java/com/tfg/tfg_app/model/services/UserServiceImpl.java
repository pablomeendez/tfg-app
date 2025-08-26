package com.tfg.tfg_app.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.entities.UsersDao;
import com.tfg.tfg_app.model.services.exceptions.IncorrectLoginException;



@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PermissionChecker permissionChecker;
    
    @Autowired
    private UsersDao userDao;

	@Override
	public List<Users> getAllUsers() {
		return userDao.findAll();
	}

    /**
	 * Sign up.
	 *
	 * @param user the user
	 * @throws DuplicateInstanceException the duplicate instance exception
	 */
    @Override
    public void signUp(Users user) throws DuplicateInstanceException {
        
        if (userDao.existsByUserName(user.getUserName())) {
            throw new DuplicateInstanceException("project.entities.user", user.getUserName());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);
    }

    /**
	 * Login.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return the user
	 * @throws IncorrectLoginException the incorrect login exception
	 */
    @Override
    @Transactional(readOnly = true)
    public Users login(String userName, String password) throws IncorrectLoginException {

        Optional<Users> user = userDao.findByUserName(userName);

        if (!user.isPresent()) {
            throw new IncorrectLoginException(userName, password);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new IncorrectLoginException(userName, password);
        }

        return user.get();
    }

    @Override
	@Transactional(readOnly = true)
	public Users checkUser(Long id) throws InstanceNotFoundException {
		return permissionChecker.checkUser(id);
	}

	/**
	 * Update profile.
	 *
	 * @param id        the id
	 * @param firstName the first name
	 * @param lastName  the last name
	 * @param email     the email
	 * @return the user
	 * @throws InstanceNotFoundException the instance not found exception
	 */
	@Override
	public Users updateProfile(Long id, String name, String lastName, String email, Boolean firstEntry)
			throws InstanceNotFoundException {

		Users user = permissionChecker.checkUser(id);

		user.setName(name);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setFirstEntry(firstEntry);

		return userDao.save(user);

	}
}