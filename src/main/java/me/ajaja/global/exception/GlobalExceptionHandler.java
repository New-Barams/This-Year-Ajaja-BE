package me.ajaja.global.exception;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException;

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
	public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
		log.warn("Validation Fail Occurs : {}", exception.getConstraintViolations());
		return ErrorResponse.from(BEAN_VALIDATION_FAIL_EXCEPTION);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(BAD_REQUEST)
	public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		log.warn("Valid Fail Occurs: {}", message);
		return ErrorResponse.withMessage(BEAN_VALIDATION_FAIL_EXCEPTION, message);
	}

	@ExceptionHandler({HttpMessageNotReadableException.class, JsonMappingException.class})
	@ResponseStatus(BAD_REQUEST)
	public ErrorResponse handleRequestMappingException(RuntimeException exception) {
		log.warn("Request Mapping Fail Occurs : {}", exception.getMessage());
		return ErrorResponse.from(INVALID_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public ErrorResponse handleUnexpectedException(RuntimeException exception) {
		log.warn("UnexpectedException Occurs : {}", exception.getMessage());
		return ErrorResponse.from(AJAJA_SERVER_ERROR);
	}
}
