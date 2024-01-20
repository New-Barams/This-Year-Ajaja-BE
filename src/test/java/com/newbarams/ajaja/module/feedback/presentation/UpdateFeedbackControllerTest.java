package com.newbarams.ajaja.module.feedback.presentation;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
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
import com.newbarams.ajaja.module.feedback.dto.FeedbackRequest;

class UpdateFeedbackControllerTest extends WebMvcTestSupport {
	private final FeedbackRequest.UpdateFeedback request
		= new FeedbackRequest.UpdateFeedback(50, "fighting");

	@ApiTest
	@DisplayName("요청 받은 값으로 유저의 피드백을 실행한다.")
	void updateFeedback_Success_WithNoException() throws Exception {
		// given
		doNothing().when(updateFeedbackService).updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post(FEEDBACK_END_POINT + "/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback")
				.tag(ApiTag.FEEDBACK)
				.summary("피드백 평가 하기 API")
				.description("해당 계획의 피드백을 진행합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void updateFeedback_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		AjajaException tokenException = new AjajaException(errorCode);
		doThrow(tokenException).when(updateFeedbackService)
			.updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post(FEEDBACK_END_POINT + "/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-invalid-token")
				.tag(ApiTag.FEEDBACK)
				.summary("피드백 평가 하기 API")
				.description("해당 계획의 피드백을 진행합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("만일 피드백 기간이 아니라면 예외를 던진다.")
	void updateFeedback_Fail_ByExpiredFeedback() throws Exception {
		// given
		AjajaException expiredException = new AjajaException(EXPIRED_FEEDBACK);
		doThrow(expiredException).when(updateFeedbackService)
			.updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post(FEEDBACK_END_POINT + "/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-expired-feedback")
				.tag(ApiTag.FEEDBACK)
				.summary("피드백 평가 하기 API")
				.description("해당 계획의 피드백을 진행합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("만일 피드백 대상 계획이 조회되지 않는다면 예외를 던진다.")
	void updateFeedback_Fail_ByPlanNotFound() throws Exception {
		// given
		AjajaException notFoundException = new AjajaException(NOT_FOUND_PLAN);
		doThrow(notFoundException).when(updateFeedbackService)
			.updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post(FEEDBACK_END_POINT + "/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-plan-not-found")
				.tag(ApiTag.FEEDBACK)
				.summary("피드백 평가 하기 API")
				.description("해당 계획의 피드백을 진행합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("해당 기간에 이미 피드백 했다면 예외를 던진다.")
	void updateFeedback_Fail_ByAlreadyFeedback() throws Exception {
		// given
		AjajaException alreadyFeedbackException = new AjajaException(ALREADY_FEEDBACK);
		doThrow(alreadyFeedbackException).when(updateFeedbackService)
			.updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post(FEEDBACK_END_POINT + "/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-already-feedback")
				.tag(ApiTag.FEEDBACK)
				.summary("피드백 평가 하기 API")
				.description("해당 계획의 피드백을 진행합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
