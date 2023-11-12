package com.newbarams.ajaja.global.common.error;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
	HttpStatus httpStatus,
	String errorName,
	String errorMessage
) {
	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage());
	}
}
