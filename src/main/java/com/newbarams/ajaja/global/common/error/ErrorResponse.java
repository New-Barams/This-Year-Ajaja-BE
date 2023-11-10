package com.newbarams.ajaja.global.common.error;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
	HttpStatus httpStatus,
	String ErrorName,
	String ErrorMessage
) {
	public static ErrorResponse from(AjajaErrorCode errorCode) {
		return new ErrorResponse(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage());
	}
}
