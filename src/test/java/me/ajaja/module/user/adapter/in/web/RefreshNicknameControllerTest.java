package me.ajaja.module.user.adapter.in.web;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import me.ajaja.common.annotation.ApiTest;
import me.ajaja.common.annotation.ParameterizedApiTest;
import me.ajaja.common.support.WebMvcTestSupport;
import me.ajaja.common.util.ApiTag;
import me.ajaja.common.util.RestDocument;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;

class RefreshNicknameControllerTest extends WebMvcTestSupport {
	private static final String REFRESH_NICKNAME_END_POINT = USER_END_POINT + "/refresh";

	@ApiTest
	@DisplayName("유효한 토큰으로 닉네임 변경 요청을 보내면 성공한다.")
	void refreshNickname_Success() throws Exception {
		// given

		// when
		var result = mockMvc.perform(post(REFRESH_NICKNAME_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE)
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("refresh-nickname-success")
				.summary("닉네임 새로고침 API")
				.description("사용자의 닉네임은 랜덤한 닉네임으로 변경합니다.")
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("요청 시 인증에 실패하면 400에러를 반환한다.")
	void refreshNickname_Fail_ByAuthentication(ErrorCode errorCode, String identifier) throws Exception {
		// given
		RuntimeException authenticationFailed = new AjajaException(errorCode);

		willThrow(authenticationFailed).given(refreshNicknameUseCase).refresh(anyLong());

		// when
		var result = mockMvc.perform(post(REFRESH_NICKNAME_END_POINT)
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
				.identifier("refresh-nickname-fail-" + identifier)
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("존재하지 않는 회원으로 요청하면 404에러를 반환한다.")
	void refreshNickname_Fail_ByNotExistUser() throws Exception {
		// given
		RuntimeException useNotFound = new AjajaException(USER_NOT_FOUND);

		willThrow(useNotFound).given(refreshNicknameUseCase).refresh(anyLong());

		// when
		var result = mockMvc.perform(post(REFRESH_NICKNAME_END_POINT)
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
				.identifier("refresh-nickname-fail-not-exist-user")
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
