package me.ajaja.module.feedback.presentation;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import me.ajaja.module.feedback.dto.FeedbackResponse;

public class GetFeedbackInfoControllerTest extends WebMvcTestSupport {

	@Test
	@DisplayName("유저의 피드백 정보들을 가져온다.")
	void getFeedbackInfo_Success_WithNoException() throws Exception {
		// given
		List<FeedbackResponse.RemindFeedback> feedbacks = List.of(
			new FeedbackResponse.RemindFeedback(50, "잘하고 있어", 6, 1, 7, 1, true),
			new FeedbackResponse.RemindFeedback(0, "", 12, 1, 12, 31, false)
		);

		FeedbackResponse.FeedbackInfo response
			= new FeedbackResponse.FeedbackInfo(2023, 50, "1일 1커밋", 9, feedbacks);

		given(loadFeedbackInfoService.loadFeedbackInfoByPlanId(anyLong(), anyLong())).willReturn(response);

		// when
		var result = mockMvc.perform(get("/feedbacks/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data.createdYear").value(response.getCreatedYear()),
			jsonPath("$.data.achieveRate").value(response.getAchieveRate()),
			jsonPath("$.data.title").value(response.getTitle()),
			jsonPath("$.data.remindTime").value(response.getRemindTime()),
			jsonPath("$.data.feedbacks[0].achieve").value(response.getFeedbacks().get(0).getAchieve()),
			jsonPath("$.data.feedbacks[0].message").value(response.getFeedbacks().get(0).getMessage()),
			jsonPath("$.data.feedbacks[0].remindMonth").value(response.getFeedbacks().get(0).getRemindMonth()),
			jsonPath("$.data.feedbacks[0].remindDate").value(response.getFeedbacks().get(0).getRemindDate()),
			jsonPath("$.data.feedbacks[0].endMonth").value(response.getFeedbacks().get(0).getEndMonth()),
			jsonPath("$.data.feedbacks[0].endDate").value(response.getFeedbacks().get(0).getEndDate()),
			jsonPath("$.data.feedbacks[0].reminded").value(response.getFeedbacks().get(0).isReminded()),
			jsonPath("$.data.feedbacks[1].achieve").value(response.getFeedbacks().get(1).getAchieve()),
			jsonPath("$.data.feedbacks[1].message").value(response.getFeedbacks().get(1).getMessage()),
			jsonPath("$.data.feedbacks[1].remindMonth").value(response.getFeedbacks().get(1).getRemindMonth()),
			jsonPath("$.data.feedbacks[1].remindDate").value(response.getFeedbacks().get(1).getRemindDate()),
			jsonPath("$.data.feedbacks[1].endMonth").value(response.getFeedbacks().get(1).getEndMonth()),
			jsonPath("$.data.feedbacks[1].endDate").value(response.getFeedbacks().get(1).getEndDate()),
			jsonPath("$.data.feedbacks[1].reminded").value(response.getFeedbacks().get(1).isReminded())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-feedbacks-success")
				.tag(ApiTag.FEEDBACK)
				.summary("피드백 정보 불러오기 API")
				.description("해당 계획의 피드백 정보들을 불러옵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void getFeedbackInfo_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		AjajaException tokenException = new AjajaException(errorCode);
		doThrow(tokenException).when(loadFeedbackInfoService).loadFeedbackInfoByPlanId(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(get("/feedbacks/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-feedbacks-fail-" + identifier)
				.tag(ApiTag.FEEDBACK)
				.secured(true)
				.result(result)
				.generateDocs());
	}

	@Test
	@DisplayName("만일 조회된 계획이 없다면 예외를 던진다.")
	void getFeedbackInfo_Fail_ByPlanNotFound() throws Exception {
		// given
		AjajaException notFoundException = new AjajaException(NOT_FOUND_PLAN);
		doThrow(notFoundException).when(loadFeedbackInfoService).loadFeedbackInfoByPlanId(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(get("/feedbacks/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-feedbacks-fail-plan-not-found")
				.tag(ApiTag.FEEDBACK)
				.secured(true)
				.result(result)
				.generateDocs());
	}
}
