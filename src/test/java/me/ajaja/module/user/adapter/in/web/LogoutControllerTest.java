package me.ajaja.module.user.adapter.in.web;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import me.ajaja.common.support.WebMvcTestSupport;
import me.ajaja.common.util.ApiTag;
import me.ajaja.common.util.RestDocument;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;

class LogoutControllerTest extends WebMvcTestSupport {
	private static final String LOGOUT_END_POINT = "/users/logout";

	@Test
	@DisplayName("유효한 토큰으로 로그아웃 요청을 보내면 성공한다.")
	void logout_Success() throws Exception {
		// given

		// when
		var result = mockMvc.perform(post(LOGOUT_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isNoContent());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("logout-success")
				.tag(ApiTag.USER)
				.summary("로그아웃 API")
				.description("발급된 사용자의 토큰을 만료시킵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedTest
	@MethodSource("authenticationFailResults")
	@DisplayName("요청 시 인증에 실패하면 400에러를 반환한다.")
	void logout_Fail_ByAuthentication(ErrorCode errorCode, String identifier) throws Exception {
		// given
		RuntimeException authenticationFailed = new AjajaException(errorCode);

		willThrow(authenticationFailed).given(logoutUseCase).logout(anyLong());

		// when
		var result = mockMvc.perform(post(LOGOUT_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.httpStatus").value(BAD_REQUEST.name()),
			jsonPath("$.errorName").value(errorCode.name()),
			jsonPath("$.errorMessage").value(errorCode.getMessage())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("logout-fail-" + identifier)
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@Test
	@DisplayName("존재하지 않는 회원으로 요청하면 404에러를 반환한다.")
	void logout_Fail_ByNotExistUser() throws Exception {
		// given
		RuntimeException useNotFound = new AjajaException(USER_NOT_FOUND);

		willThrow(useNotFound).given(logoutUseCase).logout(anyLong());

		// when
		var result = mockMvc.perform(post(LOGOUT_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isNotFound(),
			jsonPath("$.httpStatus").value(NOT_FOUND.name()),
			jsonPath("$.errorName").value(USER_NOT_FOUND.name()),
			jsonPath("$.errorMessage").value(USER_NOT_FOUND.getMessage())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("logout-fail-not-exist-user")
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
