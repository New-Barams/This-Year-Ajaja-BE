package com.newbarams.ajaja.global.mock;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "mock", description = "MOCK API")
@RestController
@RequestMapping("/mock")
class MockController {
	private static final String CERTIFICATION = "123456";
	private static final String ACCESS_TOKEN = "thisismockkakaoaccesstoken";
	private static final String REFRESH_TOKEN = "thisismockkakaorefreshtoken";
	private static final String NEW_ACCESS_TOKEN = "thisisnewmockkakaoaccesstoken";
	private static final List<String> nicknames =
		Arrays.asList("노래부르는 다람쥐", "부끄러워하는 코끼리", "춤추는 강아지", "고백하는 고양이 ", "거절하는 거북이 ", "손을 번쩍든 오리");

	@Tag(name = "mock")
	@Operation(description = "가짜 회원가입 API")
	@PostMapping("/signup")
	@ResponseStatus(OK)
	void signup() {
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 로그인 API")
	@PostMapping("/login")
	@ResponseStatus(OK)
	Map<String, String> login() {
		return new HashMap<>() {{
			put("accessToken", ACCESS_TOKEN);
			put("refreshToken", REFRESH_TOKEN);
		}};
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 Token 재발급 API")
	@PostMapping("/reissue")
	@ResponseStatus(OK)
	Map<String, String> reissue(@RequestHeader(AUTHORIZATION) String refreshToken) {
		String token = extractToken(refreshToken);
		validateRefreshToken(token);

		return new HashMap<>() {{
			put("accessToken", NEW_ACCESS_TOKEN);
		}};
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 로그아웃 API")
	@PostMapping("/logout")
	@ResponseStatus(OK)
	void logout() {
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 닉네임 새고로침 API")
	@PostMapping("/users/refresh")
	@ResponseStatus(OK)
	Map<String, String> refreshNickname(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		validateAccessToken(token);

		Collections.shuffle(nicknames);
		return new HashMap<>() {{
			put("nickname", nicknames.get(0));
		}};
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 이메일 인증 요청 API")
	@PostMapping("/users/send-verification")
	@ResponseStatus(OK)
	Map<String, String> sendVerification(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		validateAccessToken(token);

		return new HashMap<>() {{
			put("certification", CERTIFICATION);
		}};
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 이메일 인증 확인 API")
	@PostMapping("/users/verify-email")
	@ResponseStatus(OK)
	void verifyEmail(@RequestBody String certification) {
		if (!CERTIFICATION.equals(certification)) {
			throw new IllegalArgumentException("wrong certification");
		}
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 회원탈퇴 API")
	@DeleteMapping("/users")
	@ResponseStatus(OK)
	void withdraw(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		validateAccessToken(token);
	}

	private String extractToken(String token) {
		return token.substring("Bearer ".length());
	}

	private void validateAccessToken(String accessToken) {
		if (!ACCESS_TOKEN.equals(accessToken)) {
			throw new IllegalArgumentException("wrong access token");
		}
	}

	private void validateRefreshToken(String refreshToken) {
		if (!ACCESS_TOKEN.equals(refreshToken)) {
			throw new IllegalArgumentException("wrong refresh token");
		}
	}
}
