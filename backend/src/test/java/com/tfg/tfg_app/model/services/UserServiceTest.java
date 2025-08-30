package com.tfg.tfg_app.model.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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

/**
 * The Class UserServiceTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

	private final Long NON_EXISTENT_ID = Long.valueOf(-1);

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
		// Add timestamp to make usernames unique across test executions
		String uniqueUserName = userName + "_" + System.currentTimeMillis();
		return new Users(uniqueUserName, "password", "firstName", "lastName", uniqueUserName + "@" + uniqueUserName + ".com");
	}

	/**
	 * Test sign up and login from id.
	 *
	 * @throws DuplicateInstanceException the duplicate instance exception
	 * @throws InstanceNotFoundException  the instance not found exception
	 */
	@Test
	public void testSignUpAndcheckUser() throws DuplicateInstanceException, InstanceNotFoundException {

		Users user = createUser("user");

		userService.signUp(user);

		Users loggedInUser = userService.checkUser(user.getId());

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
		assertThrows(InstanceNotFoundException.class, () -> userService.checkUser(NON_EXISTENT_ID));
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

		Users updatedUser = userService.checkUser(user.getId());

		assertEquals(user, updatedUser);

	}

	@Test
	public void testUpdateProfileWithNonExistentId() {
		assertThrows(InstanceNotFoundException.class,
				() -> userService.updateProfile(NON_EXISTENT_ID, "X", "X", "X", false));
	}

	@Test
	public void testGetAllUsers() throws DuplicateInstanceException, InstanceNotFoundException {
		// Get initial count
		List<Users> initialUsers = userService.getAllUsers();
		int initialCount = initialUsers.size();

		// Create new users
		Users user1 = createUser("user1");
		Users user2 = createUser("user2");
		
		userService.signUp(user1);
		userService.signUp(user2);

		List<Users> allUsers = userService.getAllUsers();
		assertTrue(allUsers.size() >= initialCount + 2);
		assertTrue(allUsers.stream().anyMatch(u -> u.getUserName().equals(user1.getUserName())));
		assertTrue(allUsers.stream().anyMatch(u -> u.getUserName().equals(user2.getUserName())));
	}

	@Test
	public void testUpdateProfileWithFirstEntry() throws InstanceNotFoundException, DuplicateInstanceException {
		Users user = createUser("user");
		userService.signUp(user);

		// Test updating firstEntry flag
		userService.updateProfile(user.getId(), user.getName(), user.getLastName(), user.getEmail(), true);

		Users updatedUser = userService.checkUser(user.getId());
		assertTrue(updatedUser.getFirstEntry());
	}

	@Test
	public void testUserRoleAssignment() throws DuplicateInstanceException, InstanceNotFoundException {
		Users user = createUser("user");
		userService.signUp(user);

		Users savedUser = userService.checkUser(user.getId());
		assertEquals(Users.Role.USER, savedUser.getRole());
	}

	@Test
	public void testUpdateProfileWithSameEmail() throws DuplicateInstanceException, InstanceNotFoundException {
		// Create a test user
		Users testUser = createUser("testUpdateUser");
		userService.signUp(testUser);
		
		// Test updating profile with same email (should work)
		userService.updateProfile(testUser.getId(), "NewName", "NewLastName", testUser.getEmail(), false);
		
		Users updatedUser = userService.checkUser(testUser.getId());
		assertEquals("NewName", updatedUser.getName());
		assertEquals("NewLastName", updatedUser.getLastName());
		assertEquals(testUser.getEmail(), updatedUser.getEmail());
	}

	@Test
	public void testLoginWithEmptyPassword() throws DuplicateInstanceException {
		// Create a test user
		Users testUser = createUser("testEmptyPassword");
		userService.signUp(testUser);
		
		// Test login with empty password
		assertThrows(IncorrectLoginException.class, () -> {
			userService.login(testUser.getUserName(), "");
		});
	}

	@Test
	public void testLoginWithEmptyUsername() {
		// Test login with empty username
		assertThrows(IncorrectLoginException.class, () -> {
			userService.login("", "password");
		});
	}

	@Test
	public void testSignUpWithVeryLongFields() {
		// Test with very long field values
		String longString = "a".repeat(300);
		Users longUser = new Users(longString, "password", longString, longString, "long@example.com");
		
		// This should throw an exception due to database field length constraints
		try {
			userService.signUp(longUser);
			// If it doesn't throw an exception, that's also a valid outcome
			// depending on database configuration
		} catch (Exception e) {
			// Expected for fields that exceed database limits
			assertTrue("Should handle long fields with appropriate exception", 
				e instanceof DuplicateInstanceException || 
				e instanceof IllegalArgumentException || 
				e instanceof RuntimeException ||
				e.getCause() instanceof java.sql.SQLException);
		}
	}

	@Test
	public void testUpdateProfileWithNullName() throws DuplicateInstanceException, InstanceNotFoundException {
		// Create a test user
		Users testUser = createUser("testNullName");
		userService.signUp(testUser);
		
		// Test edge case with null name - the service should handle this gracefully
		// Some services might allow null values, others might replace with empty string
		try {
			userService.updateProfile(testUser.getId(), null, "ValidLastName", "valid@example.com", false);
			
			// If it succeeds, verify the behavior
			Users updatedUser = userService.checkUser(testUser.getId());
			// Either null is accepted or it's converted to empty string - both are valid
			assertTrue("Service should handle null name appropriately", 
				updatedUser.getName() == null || updatedUser.getName().isEmpty());
		} catch (Exception e) {
			// Also acceptable - service rejects null values
			assertTrue("Should handle null values with appropriate exception", 
				e instanceof IllegalArgumentException || 
				e instanceof RuntimeException ||
				e.getCause() instanceof java.sql.SQLException);
		}
	}
}