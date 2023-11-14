package com.newbarams.ajaja.global.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.newbarams.ajaja.global.common.error.ErrorCode;
import com.newbarams.ajaja.global.common.error.ErrorResponse;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(AjajaException.class)
	public ResponseEntity<ErrorResponse> handleAjajaException(AjajaException exception) {
		final ErrorCode errorCode = exception.getErrorCode();
		log.warn("{} Occurs : {}", errorCode.name(), errorCode.getMessage());
		return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponse.from(errorCode));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(BAD_REQUEST)
	public ErrorResponse handleValidationException(ConstraintViolationException exception) {
		log.warn("Validation Fail Occurs : {}", exception.getConstraintViolations());
		return ErrorResponse.from(ErrorCode.BEAN_VALIDATE_FAIL_EXCEPTION);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public ErrorResponse handleUnexpectedException(RuntimeException exception) {
		log.warn("UnexpectedException Occurs : {}", exception.getMessage());
		return ErrorResponse.from(ErrorCode.AJAJA_SERVER_ERROR);
	}
}
