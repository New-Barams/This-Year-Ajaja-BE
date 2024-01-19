package com.newbarams.ajaja.module.feedback.presentation;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.common.util.ApiTag;
import com.newbarams.ajaja.common.util.RestDocument;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.feedback.dto.FeedbackRequest;

class UpdateFeedbackControllerTest extends WebMvcTestSupport {
	private final FeedbackRequest.UpdateFeedback request
		= new FeedbackRequest.UpdateFeedback(50, "fighting");

	@ApiTest
	void updateFeedback_Success() throws Exception {
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

	@ApiTest
	void updateFeedback_Fail_InvalidToken() throws Exception {
		// given
		AjajaException tokenException = new AjajaException(INVALID_TOKEN);
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
	void updateFeedback_Fail_ExpiredFeedback() throws Exception {
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
	void updateFeedback_Fail_PlanNotFound() throws Exception {
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
	void updateFeedback_Fail_alreadyFeedback() throws Exception {
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
