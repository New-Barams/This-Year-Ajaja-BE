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
	NOT_FOUND_FEEDBACK(BAD_REQUEST, "피드백 정보가 존재하지 않습니다."),
	NOT_FOUND_PLAN(BAD_REQUEST, "계획 정보가 존재하지 않습니다."),
	EXPIRED_FEEDBACK(CONFLICT, "평가 기간이 지났습니다."),
	INVALID_BEARER_FORMAT(BAD_REQUEST, "유효한 Bearer 토큰의 형식이 아닙니다."),
	INVALID_JWT_SIGNATURE(BAD_REQUEST, "잘못된 서명입니다."),
	INVALID_JWT(BAD_REQUEST, "잘못된 토큰입니다."),
	EXPIRED_JWT(BAD_REQUEST, "만료된 토큰입니다."),
	UNSUPPORTED_JWT(BAD_REQUEST, "지원하지 않는 토큰입니다."),
	ALREADY_EMAIL_VERIFIED(BAD_REQUEST, "이미 이메일 인증을 진행한 사용자입니다."),

	// 404
	USER_NOT_FOUND(NOT_FOUND, "사용자가 존재하지 않습니다."),

	// 500
	AJAJA_SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류입니다. 관리자에게 문의바랍니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
