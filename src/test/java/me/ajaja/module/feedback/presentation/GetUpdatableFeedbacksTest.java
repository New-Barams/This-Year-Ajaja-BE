package me.ajaja.module.feedback.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import me.ajaja.module.feedback.dto.FeedbackResponse;

public class GetUpdatableFeedbacksTest extends WebMvcTestSupport {
	private final List<FeedbackResponse.UpdatableFeedback> response
		= List.of(
		new FeedbackResponse.UpdatableFeedback("계획1", 1L, 0, 5, 1),
		new FeedbackResponse.UpdatableFeedback("계획2", 2L, 13, 5, 12)
	);

	@ApiTest
	@DisplayName("업데이트 가능한 피드백 목록들을 불러온다.")
	void getUpdatableFeedbacks_Success_WithNoException() throws Exception {
		// given
		given(loadUpdatableFeedbackService.loadUpdatableFeedbacksByUserId(anyLong(), any())).willReturn(response);

		// when
		var result = mockMvc.perform(get(FEEDBACK_END_POINT + "/updatable")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data[0].title").value(response.get(0).getTitle()),
			jsonPath("$.data[0].planId").value(response.get(0).getPlanId()),
			jsonPath("$.data[0].remainPeriod").value(response.get(0).getRemainPeriod()),
			jsonPath("$.data[0].month").value(response.get(0).getMonth()),
			jsonPath("$.data[0].date").value(response.get(0).getDate()),
			jsonPath("$.data[1].title").value(response.get(1).getTitle()),
			jsonPath("$.data[1].planId").value(response.get(1).getPlanId()),
			jsonPath("$.data[1].remainPeriod").value(response.get(1).getRemainPeriod()),
			jsonPath("$.data[1].month").value(response.get(1).getMonth()),
			jsonPath("$.data[1].date").value(response.get(1).getDate())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-updatable-feedback")
				.tag(ApiTag.FEEDBACK)
				.summary("진행하지 않은 피드백 불러오기")
				.description("메인페이지에서 진행하지 않은 피드백들을 불러온다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void getFeedbackInfo_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		AjajaException tokenException = new AjajaException(errorCode);
		doThrow(tokenException).when(loadUpdatableFeedbackService).loadUpdatableFeedbacksByUserId(anyLong(), any());

		// when
		var result = mockMvc.perform(get(FEEDBACK_END_POINT + "/updatable")
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
				.identifier("get-updatable-feedbacks-fail-" + identifier)
				.tag(ApiTag.FEEDBACK)
				.secured(true)
				.result(result)
				.generateDocs());
	}
}
