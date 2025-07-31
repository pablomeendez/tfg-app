package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import jakarta.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.exceptions.IncorrectLoginException;
import com.tfg.tfg_app.model.services.exceptions.IncorrectPasswordException;

/**
 * The Class UserServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

	private final Long NON_EXISTENT_ID = Long.valueOf(-1);
	private final String NON_EXISTENT_USERNAME = "nonExistentUsername";

	/** The user service. */
	@Autowired
	private UserService userService;

	/**
	 * Creates the user.
	 *
	 * @param userName the user name
	 * @return the user
	 */
	private Users createUser(String userName) {
		return new Users(userName, "password", "firstName", "lastName", userName + "@" + userName + ".com");
	}

	/**
	 * Test sign up and login from id.
	 *
	 * @throws DuplicateInstanceException the duplicate instance exception
	 * @throws InstanceNotFoundException  the instance not found exception
	 */
	@Test
	public void testSignUpAndLoginFromId() throws DuplicateInstanceException, InstanceNotFoundException {

		Users user = createUser("user");

		userService.signUp(user);

		Users loggedInUser = userService.loginFromId(user.getId());

		assertEquals(user, loggedInUser);

	}

	@Test
	public void testSignUpDuplicatedUserName() throws DuplicateInstanceException {

		Users user = createUser("user");

		userService.signUp(user);
		assertThrows(DuplicateInstanceException.class, () -> userService.signUp(user));

	}

	@Test
	public void testLoginFromNonExistentId() {
		assertThrows(InstanceNotFoundException.class, () -> userService.loginFromId(NON_EXISTENT_ID));
	}

	@Test
	public void testLogin() throws DuplicateInstanceException, IncorrectLoginException {

		Users user = createUser("user");
		String clearPassword = user.getPassword();

		userService.signUp(user);

		Users loggedInUser = userService.login(user.getUserName(), clearPassword);

		assertEquals(user, loggedInUser);

	}

	@Test
	public void testLoginWithIncorrectPassword() throws DuplicateInstanceException {

		Users user = createUser("user");
		String clearPassword = user.getPassword();

		userService.signUp(user);
		assertThrows(IncorrectLoginException.class, () -> userService.login(user.getUserName(), 'X' + clearPassword));

	}

	@Test
	public void testLoginWithNonExistentUserName() {
		assertThrows(IncorrectLoginException.class, () -> userService.login("X", "Y"));
	}

	@Test
	public void testUpdateProfile() throws InstanceNotFoundException, DuplicateInstanceException {

		Users user = createUser("user");

		userService.signUp(user);

		user.setName('X' + user.getName());
		user.setLastName('X' + user.getLastName());
		user.setEmail('X' + user.getEmail());

		userService.updateProfile(user.getId(), 'X' + user.getName(), 'X' + user.getLastName(),
				'X' + user.getEmail(), false);

		Users updatedUser = userService.loginFromId(user.getId());

		assertEquals(user, updatedUser);

	}

	@Test
	public void testUpdateProfileWithNonExistentId() {
		assertThrows(InstanceNotFoundException.class,
				() -> userService.updateProfile(NON_EXISTENT_ID, "X", "X", "X", false));
	}

	@Test
	public void testChangePassword() throws DuplicateInstanceException, InstanceNotFoundException,
			IncorrectPasswordException, IncorrectLoginException {

		Users user = createUser("user");
		String oldPassword = user.getPassword();
		String newPassword = 'X' + oldPassword;

		userService.signUp(user);
		userService.changePassword(user.getId(), oldPassword, newPassword);
		userService.login(user.getUserName(), newPassword);

	}

	@Test
	public void testChangePasswordWithNonExistentId() {
		assertThrows(InstanceNotFoundException.class, () -> userService.changePassword(NON_EXISTENT_ID, "X", "Y"));
	}

	@Test
	public void testChangePasswordWithIncorrectPassword() throws DuplicateInstanceException {

		Users user = createUser("user");
		String oldPassword = user.getPassword();
		String newPassword = 'X' + oldPassword;

		userService.signUp(user);
		assertThrows(IncorrectPasswordException.class,
				() -> userService.changePassword(user.getId(), 'Y' + oldPassword, newPassword));

	}
}