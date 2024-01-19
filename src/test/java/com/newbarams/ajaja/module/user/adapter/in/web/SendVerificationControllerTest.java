package com.newbarams.ajaja.module.user.adapter.in.web;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.annotation.ParameterizedApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.common.util.ApiTag;
import com.newbarams.ajaja.common.util.RestDocument;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.exception.ErrorCode;
import com.newbarams.ajaja.module.user.dto.UserRequest;

class SendVerificationControllerTest extends WebMvcTestSupport {
	private static final String SEND_VERIFICATION_END_POINT = USER_END_POINT + "/send-verification";

	private final UserRequest.EmailVerification request = new UserRequest.EmailVerification("ajaja@me.com");

	@ApiTest
	@DisplayName("검증 데이터가 일치하면 요청에 성공한다.")
	void sendVerification_Success() throws Exception {
		// given

		// when
		var result = mockMvc.perform(post(SEND_VERIFICATION_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isNoContent());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("send-verify-email")
				.tag(ApiTag.USER)
				.summary("이메일 검증 요청 API")
				.description("리마인드를 받을 이메일을 검증하기 위해 인증을 요청합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationResults")
	@DisplayName("요청 시 인증에 실패하면 400에러를 반환한다.")
	void sendVerification_Fail_ByAuthentication(ErrorCode errorCode, String identifier) throws Exception {
		// given
		RuntimeException authenticationFailed = new AjajaException(errorCode);

		willThrow(authenticationFailed).given(sendVerificationEmailUseCase).sendVerification(anyLong(), anyString());

		// when
		var result = mockMvc.perform(post(SEND_VERIFICATION_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

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
				.identifier("send-verification-fail-" + identifier)
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("존재하지 않는 회원으로 요청하면 404에러를 반환한다.")
	void sendVerification_Fail_ByNotExistUser() throws Exception {
		// given
		RuntimeException useNotFound = new AjajaException(USER_NOT_FOUND);

		willThrow(useNotFound).given(sendVerificationEmailUseCase).sendVerification(anyLong(), anyString());

		// when
		var result = mockMvc.perform(post(SEND_VERIFICATION_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

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
				.identifier("send-verification-fail-not-exist-user")
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("이미 인증된 완료된 이메일로 요청하면 409에러를 반환한다.")
	void sendVerification_Fail_ByAlreadyVerified() throws Exception {
		// given
		RuntimeException alreadyVerified = new AjajaException(ALREADY_VERIFY_EMAIL);

		willThrow(alreadyVerified).given(sendVerificationEmailUseCase).sendVerification(anyLong(), anyString());

		// when
		var result = mockMvc.perform(post(SEND_VERIFICATION_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isConflict(),
			jsonPath("$.httpStatus").value(CONFLICT.name()),
			jsonPath("$.errorName").value(ALREADY_VERIFY_EMAIL.name()),
			jsonPath("$.errorMessage").value(ALREADY_VERIFY_EMAIL.getMessage())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("send-verification-fail-already-verified")
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
