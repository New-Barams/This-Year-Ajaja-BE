package com.newbarams.ajaja.module.remind.adapter.in.web;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
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

class SendTestRemindControllerTest extends WebMvcTestSupport {

	@ApiTest
	void sendTestRemind_Success_WithNoException() throws Exception {
		// given
		given(sendTestRemindUseCase.send(anyLong())).willReturn("EMAIL");

		// when
		var result = mockMvc.perform(post(REMIND_END_POINT + "/test")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data").value("EMAIL")
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("send-test-remind")
				.tag(ApiTag.REMIND)
				.summary("리마인드 미리 보내보기 API")
				.description("미래에 받아볼 리마인드 형식을 미리 받아볼 수 있습니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void sendTestRemind_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		AjajaException tokenException = new AjajaException(errorCode);
		when(sendTestRemindUseCase.send(anyLong())).thenThrow(tokenException);

		// when
		var result = mockMvc.perform(post(REMIND_END_POINT + "/test")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("send-test-remind-invalid-token")
				.tag(ApiTag.REMIND)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void sendTestRemind_Fail_ByRequestOverMax() throws Exception {
		// given
		AjajaException requestException = new AjajaException(REQUEST_OVER_MAX);
		when(sendTestRemindUseCase.send(1L)).thenThrow(requestException);

		// when
		var result = mockMvc.perform(post(REMIND_END_POINT + "/test")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("send-test-remind-request-over-max")
				.tag(ApiTag.REMIND)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void sendTestRemind_Fail_ByTaskFailed() throws Exception {
		// given
		AjajaException taskFailedException = new AjajaException(REMIND_TASK_FAILED);
		when(sendTestRemindUseCase.send(1L)).thenThrow(taskFailedException);

		// when
		var result = mockMvc.perform(post(REMIND_END_POINT + "/test")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("send-test-remind-task-failed")
				.tag(ApiTag.REMIND)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
