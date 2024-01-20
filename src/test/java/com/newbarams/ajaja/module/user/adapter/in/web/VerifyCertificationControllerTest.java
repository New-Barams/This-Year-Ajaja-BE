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

class VerifyCertificationControllerTest extends WebMvcTestSupport {
	private static final String VERIFY_END_POINT = USER_END_POINT + "/verify";

	private final UserRequest.Certification request = new UserRequest.Certification("123456");

	@ApiTest
	@DisplayName("검증 데이터가 일치하면 요청에 성공한다.")
	void verifyCertification_Success() throws Exception {
		// given

		// when
		var result = mockMvc.perform(post(VERIFY_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("verify-certification-success")
				.tag(ApiTag.USER)
				.summary("인증 번호 검증 API")
				.description("발송된 인증 번호를 검증합니다. 인증번호는 6자리 숫자로 이루어져 있습니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("요청 시 인증에 실패하면 400에러를 반환한다.")
	void verifyCertification_Fail_ByAuthentication(ErrorCode errorCode, String identifier) throws Exception {
		// given
		RuntimeException authenticationFailed = new AjajaException(errorCode);

		willThrow(authenticationFailed).given(verifyCertificationUseCase).verify(anyLong(), anyString());

		// when
		var result = mockMvc.perform(post(VERIFY_END_POINT)
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
				.identifier("verify-certification-fail-" + identifier)
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("존재하지 않는 회원으로 요청하면 404에러를 반환한다.")
	void verifyCertification_Fail_ByNotExistUser() throws Exception {
		// given
		RuntimeException useNotFound = new AjajaException(USER_NOT_FOUND);

		willThrow(useNotFound).given(verifyCertificationUseCase).verify(anyLong(), anyString());

		// when
		var result = mockMvc.perform(post(VERIFY_END_POINT)
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
				.identifier("verify-certification-fail-not-exist-user")
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("잘못된 인증 번호로 요청하면 409에러를 반환한다.")
	void verifyCertification_Fail_ByCertificationNotMatch() throws Exception {
		// given
		RuntimeException certificationNotMatch = new AjajaException(CERTIFICATION_NOT_MATCH);

		willThrow(certificationNotMatch).given(verifyCertificationUseCase).verify(anyLong(), anyString());

		// when
		var result = mockMvc.perform(post(VERIFY_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isConflict(),
			jsonPath("$.httpStatus").value(CONFLICT.name()),
			jsonPath("$.errorName").value(CERTIFICATION_NOT_MATCH.name()),
			jsonPath("$.errorMessage").value(CERTIFICATION_NOT_MATCH.getMessage())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("verify-certification-fail-certification-not-match")
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
