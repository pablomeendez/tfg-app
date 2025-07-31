package com.tfg.tfg_app.rest.common;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tfg.tfg_app.model.common.exceptions.DuplicateInstanceException;
import com.tfg.tfg_app.model.common.exceptions.InstanceNotFoundException;
import com.tfg.tfg_app.model.services.exceptions.DuplicatedEntryException;
import com.tfg.tfg_app.model.services.exceptions.IncorrectLoginException;
import com.tfg.tfg_app.model.services.exceptions.IncorrectPasswordException;
import com.tfg.tfg_app.model.services.exceptions.PermissionException;
import com.tfg.tfg_app.model.services.exceptions.TrophyAlreadyGivenException;

import jakarta.validation.ConstraintViolationException;

/**
 * The Class CommonControllerAdvice.
 */
@ControllerAdvice
public class CommonControllerAdvice {

	/** The Constant INSTANCE_NOT_FOUND_EXCEPTION_CODE. */
	private static final String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";

	/** The Constant DUPLICATE_INSTANCE_EXCEPTION_CODE. */
	private static final String DUPLICATE_INSTANCE_EXCEPTION_CODE = "project.exceptions.DuplicateInstanceException";

	/** The Constant PERMISSION_EXCEPTION_CODE. */
	private static final String PERMISSION_EXCEPTION_CODE = "project.exceptions.PermissionException";

	/** The Constant INCORRECT_LOGIN_EXCEPTION_CODE. */
	private static final String INCORRECT_LOGIN_EXCEPTION_CODE = "project.exceptions.IncorrectLoginException";

	/** The Constant INCORRECT_PASSWORD_EXCEPTION_CODE. */
	private static final String INCORRECT_PASS_EXCEPTION_CODE = "project.exceptions.IncorrectPasswordException";

	private static final String CONSTRAINT_VIOLATION_EXCEPTION_CODE = "project.exceptions.ConstraintViolationException";

	private static final String DUPLICATED_ENTRY_EXCEPTION_CODE = "project.exceptions.DuplicatedEntryException";

	/** The message source. */
	@Autowired
	private MessageSource messageSource;

	/**
	 * Handle method argument not valid exception.
	 *
	 * @param exception the exception
	 * @return the errors dto
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

		List<FieldErrorDto> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage()))
				.collect(Collectors.toList());

		return new ErrorsDto(fieldErrors);

	}

	/**
	 * Handle instance not found exception.
	 *
	 * @param exception the exception
	 * @param locale    the locale
	 * @return the errors dto
	 */
	@ExceptionHandler(InstanceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {

		String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE,
				new Object[] { nameMessage, exception.getKey().toString() }, INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	/**
	 * Handle duplicate instance exception.
	 *
	 * @param exception the exception
	 * @param locale    the locale
	 * @return the errors dto
	 */
	@ExceptionHandler(DuplicateInstanceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleDuplicateInstanceException(DuplicateInstanceException exception, Locale locale) {

		String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		String errorMessage = messageSource.getMessage(DUPLICATE_INSTANCE_EXCEPTION_CODE,
				new Object[] { nameMessage, exception.getKey().toString() }, DUPLICATE_INSTANCE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	/**
	 * Handle permission exception.
	 *
	 * @param exception the exception
	 * @param locale    the locale
	 * @return the errors dto
	 */
	@ExceptionHandler(PermissionException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorsDto handlePermissionException(PermissionException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(PERMISSION_EXCEPTION_CODE, null, PERMISSION_EXCEPTION_CODE,
				locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleConstraintViolationException(ConstraintViolationException exception, Locale locale) {
		String errorMessage = messageSource.getMessage(CONSTRAINT_VIOLATION_EXCEPTION_CODE, null,
				CONSTRAINT_VIOLATION_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
	}
		/**
	 * Handle incorrect login exception.
	 *
	 * @param exception the exception
	 * @param locale    the locale
	 * @return the errors dto
	 */
	@ExceptionHandler(IncorrectLoginException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(INCORRECT_LOGIN_EXCEPTION_CODE, null,
				INCORRECT_LOGIN_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

		/**
	 * Handle incorrect password exception.
	 *
	 * @param exception the exception
	 * @param locale    the locale
	 * @return the errors dto
	 */
	@ExceptionHandler(IncorrectPasswordException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectPasswordException(IncorrectPasswordException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(INCORRECT_PASS_EXCEPTION_CODE, null,
				INCORRECT_PASS_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(DuplicatedEntryException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleDuplicatedEntryException(DuplicatedEntryException exception, Locale locale) {

		String errorMessage = messageSource.getMessage(DUPLICATED_ENTRY_EXCEPTION_CODE, null,
				DUPLICATED_ENTRY_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(TrophyAlreadyGivenException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleTrophyAlreadyGivenException(TrophyAlreadyGivenException exception, Locale locale) {
		String errorMessage = messageSource.getMessage("project.exceptions.TrophyAlreadyGivenException",
				new Object[] { exception.getTrophyId(), exception.getUserId() }, "Trophy already given",
				locale);

		return new ErrorsDto(errorMessage);
	}
}
