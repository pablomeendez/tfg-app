package com.tfg.tfg_app.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.entities.Users;
import com.tfg.tfg_app.model.services.UserService;
import com.tfg.tfg_app.model.services.exceptions.IncorrectLoginException;
import com.tfg.tfg_app.rest.common.JwtGenerator;
import com.tfg.tfg_app.rest.common.JwtInfo;
import com.tfg.tfg_app.rest.dtos.AuthenticatedUserDto;
import com.tfg.tfg_app.rest.dtos.LoginParamsDto;
import com.tfg.tfg_app.rest.dtos.UserDto;

import static com.tfg.tfg_app.rest.dtos.UserConversor.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<AuthenticatedUserDto> signUp(
        @Validated({ UserDto.AllValidations.class }) @RequestBody UserDto userDto) throws DuplicateInstanceException {

            Users user = toUser(userDto);

            userService.signUp(user);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId())
            .toUri();

    return ResponseEntity.created(location).body(toAuthenticatedUserDto(generateServiceToken(user), user));

    }

	@PutMapping("/{userId}")
	public AuthenticatedUserDto updateUser(
			@RequestAttribute Long userId,
			@Validated({ UserDto.UpdateValidations.class }) @RequestBody UserDto userDto)
			throws InstanceNotFoundException, DuplicateInstanceException {

		Users user = toUser(userDto);
		userService.updateProfile(userId, user.getName(), user.getLastName(), user.getEmail(), user.getFirstEntry());

		return toAuthenticatedUserDto(generateServiceToken(user), user);
	}

	/**
	 * Login.
	 *
	 * @param params the params
	 * @return the authenticated user dto
	 * @throws IncorrectLoginException the incorrect login exception
	 */
	@PostMapping("/login")
	public AuthenticatedUserDto login(@Validated @RequestBody LoginParamsDto params) throws IncorrectLoginException {

		Users user = userService.login(params.getUserName(), params.getPassword());

		return toAuthenticatedUserDto(generateServiceToken(user), user);
	}

	@PostMapping("/loginFromServiceToken")
	public AuthenticatedUserDto loginFromServiceToken(@RequestAttribute Long userId,
			@RequestAttribute String serviceToken) throws InstanceNotFoundException {

		Users user = userService.loginFromId(userId);

		return toAuthenticatedUserDto(serviceToken, user);

	}

    	/**
	 * Generate service token.
	 *
	 * @param user the user
	 * @return the string
	 */
	private String generateServiceToken(Users user) {

		JwtInfo jwtInfo = new JwtInfo(user.getId(), user.getUserName(), user.getRole().toString());

		return jwtGenerator.generate(jwtInfo);

	}
}
