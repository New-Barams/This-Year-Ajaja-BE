package com.newbarams.ajaja.module.user.adapter.in.web;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
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
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.dto.UserRequest;

class ChangeRemindTypeControllerTest extends WebMvcTestSupport {
	private static final String CHANGE_RECEIVE_END_POINT = USER_END_POINT + "/receive";

	private final UserRequest.Receive request = new UserRequest.Receive(User.RemindType.BOTH);

	@ApiTest
	@DisplayName("지원하는 타입으로 리마인드 수신 타입 변경 요청을 보내면 성공한다.")
	void changeRemindType_Success() throws Exception {
		// given
		willDoNothing().given(changeRemindTypeUseCase).change(anyLong(), any());

		// when
		var result = mockMvc.perform(put(CHANGE_RECEIVE_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isOk()
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-remind-type-success")
				.tag(ApiTag.USER)
				.summary("수신 방법 변경 API")
				.description("리마인드를 수신할 방법을 변경합니다. 지원하는 타입은 [KAKAO, EMAIL, BOTH] 입니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("요청 시 인증에 실패하면 400에러를 반환한다.")
	void changeRemindType_Fail_ByAuthentication(ErrorCode errorCode, String identifier) throws Exception {
		// given
		RuntimeException authenticationFailed = new AjajaException(errorCode);

		willThrow(authenticationFailed).given(changeRemindTypeUseCase).change(anyLong(), any());

		// when
		var result = mockMvc.perform(put(CHANGE_RECEIVE_END_POINT)
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
				.identifier("change-receive-fail-" + identifier)
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("존재하지 않는 회원으로 요청하면 404에러를 반환한다.")
	void changeRemindType_Fail_ByNotExistUser() throws Exception {
		// given
		RuntimeException useNotFound = new AjajaException(USER_NOT_FOUND);

		willThrow(useNotFound).given(changeRemindTypeUseCase).change(anyLong(), any());

		// when
		var result = mockMvc.perform(put(CHANGE_RECEIVE_END_POINT)
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
				.identifier("change-receive-fail-not-exist-user")
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
