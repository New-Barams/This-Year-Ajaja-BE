package com.newbarams.ajaja.global.common.exeption;

import com.newbarams.ajaja.global.common.error.AjajaErrorCode;

import lombok.Getter;

@Getter
public class AjajaException extends RuntimeException {
	private final AjajaErrorCode errorCode;

	public AjajaException(AjajaErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public AjajaException(String message, AjajaErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public static AjajaException withId(Long id, AjajaErrorCode errorCode) {
		String message = errorCode.getMessage() + " Wrong with Id :" + id;

		return new AjajaException(message, errorCode);
	}

	public AjajaException(String message, Throwable cause, AjajaErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}
}
