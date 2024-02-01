package me.ajaja.global.exception;

import lombok.Getter;

@Getter
public class AjajaException extends RuntimeException {
	private final ErrorCode errorCode;

	public AjajaException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public AjajaException(Throwable cause, ErrorCode errorCode) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}

	private AjajaException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public static AjajaException withId(Long id, ErrorCode errorCode) {
		String message = errorCode.getMessage() + " Wrong with Id :" + id;
		return new AjajaException(message, errorCode);
	}

	public int getHttpStatus() {
		return errorCode.getHttpStatus().value();
	}
}
