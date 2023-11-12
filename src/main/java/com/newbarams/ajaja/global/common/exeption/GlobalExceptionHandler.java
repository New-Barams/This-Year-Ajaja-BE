package com.newbarams.ajaja.global.common.exeption;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.newbarams.ajaja.global.common.error.ErrorCode;
import com.newbarams.ajaja.global.common.error.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(AjajaException.class)
	public ErrorResponse handleAjajaException(AjajaException exception) {
		ErrorCode errorCode = exception.getErrorCode();

		return ErrorResponse.from(errorCode);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ErrorResponse handleValidationException(ConstraintViolationException exception) {
		return ErrorResponse.from(ErrorCode.BEAN_VALIDATE_FAIL_EXCEPTION);
	}

	@ExceptionHandler(RuntimeException.class)
	public ErrorResponse handleUnexpectedException(RuntimeException exception) {
		return ErrorResponse.from(ErrorCode.AJAJA_SERVER_ERROR);
	}
}
