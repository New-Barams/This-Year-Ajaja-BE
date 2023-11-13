package com.newbarams.ajaja.global.common.error;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum AjajaErrorCode {
	// 400
	BEAN_VALIDATE_FAIL_EXCEPTION(BAD_REQUEST, "올바르지 않은 데이터입니다."),
	NOT_FOUND_FEEDBACK(BAD_REQUEST, "피드백 정보가 존재하지 않습니다."),
	NOT_FOUND_PLAN(BAD_REQUEST, "계획 정보가 존재하지 않습니다."),
	EXPIRED_FEEDBACK(CONFLICT, "평가 기간이 지났습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	AjajaErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
