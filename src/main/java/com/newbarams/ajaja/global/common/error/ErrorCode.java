package com.newbarams.ajaja.global.common.error;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// 400
	BEAN_VALIDATE_FAIL_EXCEPTION(BAD_REQUEST, "올바르지 않은 데이터입니다."),
	INVALID_BEARER_FORMAT(BAD_REQUEST, "유효한 Bearer 토큰의 형식이 아닙니다."),
	INVALID_JWT_SIGNATURE(BAD_REQUEST, "잘못된 서명입니다."),
	INVALID_JWT(BAD_REQUEST, "잘못된 토큰입니다."),
	EXPIRED_JWT(BAD_REQUEST, "만료된 토큰입니다."),
	UNSUPPORTED_JWT(BAD_REQUEST, "지원하지 않는 토큰입니다."),

	// 404
	USER_NOT_FOUND(NOT_FOUND, "사용자가 존재하지 않습니다."),

	// 500
	AJAJA_SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류입니다. 관리자에게 문의바랍니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
