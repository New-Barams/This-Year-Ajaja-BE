package me.ajaja.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// 400
	BEAN_VALIDATION_FAIL_EXCEPTION(BAD_REQUEST, "올바르지 않은 데이터입니다."),
	INVALID_REQUEST(BAD_REQUEST, "올바른 형식의 데이터가 입력되지 않았습니다."),
	INVALID_BEARER_TOKEN(BAD_REQUEST, "유효한 Bearer 토큰의 형식이 아닙니다."),
	INVALID_SIGNATURE(BAD_REQUEST, "잘못된 서명입니다."),
	INVALID_TOKEN(BAD_REQUEST, "잘못된 토큰입니다."),
	EMPTY_TOKEN(BAD_REQUEST, "비어있는 토큰입니다."),
	EXPIRED_TOKEN(BAD_REQUEST, "만료된 토큰입니다."),
	UNSUPPORTED_TOKEN(BAD_REQUEST, "지원하지 않는 토큰입니다."),
	TOKEN_NOT_MATCH(BAD_REQUEST, "일치하지 않는 토큰입니다."),
	NOT_AUTHOR(BAD_REQUEST, "잘못된 유저의 접근입니다."),
	UNMODIFIABLE_DURATION(BAD_REQUEST, "변경 가능한 기간이 아닙니다."),
	EXCEED_MAX_NUMBER_OF_PLANS(BAD_REQUEST, "유저가 가질 수 있는 최대 계획 개수를 초과하였습니다."),
	EXPIRED_FEEDBACK(BAD_REQUEST, "피드백 기간이 아닙니다."),
	NON_NUMERIC_INPUT(BAD_REQUEST, "휴대폰 번호로 숫자 이외의 값을 입력할 수 없습니다."),
	REMIND_TASK_FAILED(BAD_REQUEST, "리마인드 전송에 실패하였습니다."), // todo: product code에서 사용하지 않고 있음.

	// 401
	AUTHORIZATION_NOT_PROCESSED(UNAUTHORIZED, "인가 처리가 완료되지 않은 요청입니다."),

	// 404
	NOT_SUPPORT_END_POINT(NOT_FOUND, "지원하지 않는 API 입니다."),
	USER_NOT_FOUND(NOT_FOUND, "사용자가 존재하지 않습니다."),
	EMPTY_CACHE(NOT_FOUND, "캐시에 저장된 값이 없습니다."),
	NOT_FOUND_PLAN(NOT_FOUND, "계획 정보가 존재하지 않습니다."),

	// 409
	ALREADY_FEEDBACK(CONFLICT, "이미 평가된 피드백 정보가 있습니다."),
	ALREADY_VERIFY_EMAIL(CONFLICT, "이메일 인증을 할 수 없습니다. 인증이 완료된 상태라면 기존 리마인드 이메일과 다른 이메일을 입력해야 합니다."),
	CERTIFICATION_NOT_MATCH(CONFLICT, "인증 번호가 일치하지 않습니다."),

	// 429
	OVER_FREE_TRIAL(TOO_MANY_REQUESTS, "테스트 리마인드는 하루에 3회만 보낼 수 있습니다."),

	// 500
	AJAJA_SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류입니다. 관리자에게 문의바랍니다."),
	EXTERNAL_API_FAIL(INTERNAL_SERVER_ERROR, "외부 API 호출에 실패했습니다."),
	EXCEED_MAX_TRY(INTERNAL_SERVER_ERROR, "외부 API 최대 재시도 횟수를 초과하였습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
