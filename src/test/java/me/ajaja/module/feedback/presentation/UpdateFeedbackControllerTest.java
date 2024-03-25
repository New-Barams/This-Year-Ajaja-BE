package me.ajaja.module.feedback.presentation;

import static me.ajaja.global.exception.ErrorCode.*;
import static me.ajaja.module.feedback.dto.FeedbackRequest.*;
import static org.mockito.BDDMockito.*;
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

class UpdateFeedbackControllerTest extends WebMvcTestSupport {
	private final UpdateFeedback request = new UpdateFeedback(50, "fighting");

	@Test
	@DisplayName("요청 받은 값으로 유저의 피드백을 실행한다.")
	void updateFeedback_Success_WithNoException() throws Exception {
		// given
		doNothing().when(updateFeedbackService).updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post("/feedbacks/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-success")
				.tag(ApiTag.FEEDBACK)
				.summary("피드백 평가 하기 API")
				.description("해당 계획의 피드백을 진행합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void updateFeedback_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		AjajaException tokenException = new AjajaException(errorCode);
		doThrow(tokenException).when(updateFeedbackService)
			.updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post("/feedbacks/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-fail-" + identifier)
				.tag(ApiTag.FEEDBACK)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@Test
	@DisplayName("만일 피드백 기간이 아니라면 예외를 던진다.")
	void updateFeedback_Fail_ByExpiredFeedback() throws Exception {
		// given
		AjajaException expiredException = new AjajaException(EXPIRED_FEEDBACK);
		willThrow(expiredException).given(updateFeedbackService)
			.updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post("/feedbacks/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-fail-expired-feedback")
				.tag(ApiTag.FEEDBACK)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@Test
	@DisplayName("만일 피드백 대상 계획이 조회되지 않는다면 예외를 던진다.")
	void updateFeedback_Fail_ByPlanNotFound() throws Exception {
		// given
		AjajaException notFoundException = new AjajaException(NOT_FOUND_PLAN);
		willThrow(notFoundException).given(updateFeedbackService)
			.updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post("/feedbacks/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-fail-plan-not-found")
				.tag(ApiTag.FEEDBACK)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@Test
	@DisplayName("해당 기간에 이미 피드백 했다면 예외를 던진다.")
	void updateFeedback_Fail_ByAlreadyFeedback() throws Exception {
		// given
		AjajaException alreadyFeedbackException = new AjajaException(ALREADY_FEEDBACK);
		doThrow(alreadyFeedbackException).when(updateFeedbackService)
			.updateFeedback(anyLong(), anyLong(), anyInt(), anyString());

		// when
		var result = mockMvc.perform(post("/feedbacks/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-feedback-fail-already-feedback")
				.tag(ApiTag.FEEDBACK)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
