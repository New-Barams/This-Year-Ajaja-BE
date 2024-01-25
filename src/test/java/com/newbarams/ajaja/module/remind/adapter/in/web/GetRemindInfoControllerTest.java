package com.newbarams.ajaja.module.remind.adapter.in.web;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

class GetRemindInfoControllerTest extends WebMvcTestSupport {

	@ApiTest
	@DisplayName("해당 계획의 리마인드 정보들을 가져온다.")
	void getRemindResponse_Success_WithNoException() throws Exception {
		// given
		List<RemindResponse.Message> messages = List.of(
			new RemindResponse.Message("화이팅", 12, 1, true)
		);

		RemindResponse.RemindInfo response = new RemindResponse.RemindInfo(
			"MORNING", true, 12, 12, 1, messages
		);

		given(findPlanRemindQuery.findByUserIdAndPlanId(anyLong(), anyLong())).willReturn(response);

		// when
		var result = mockMvc.perform(get(REMIND_END_POINT + "/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data.remindTime").value(response.getRemindTime()),
			jsonPath("$.data.remindable").value(response.isRemindable()),
			jsonPath("$.data.totalPeriod").value(response.getTotalPeriod()),
			jsonPath("$.data.remindTerm").value(response.getRemindTerm()),
			jsonPath("$.data.remindDate").value(response.getRemindDate()),
			jsonPath("$.data.messageResponses[0].remindMessage")
				.value(response.getMessageResponses().get(0).getRemindMessage()),
			jsonPath("$.data.messageResponses[0].remindMonth")
				.value(response.getMessageResponses().get(0).getRemindMonth()),
			jsonPath("$.data.messageResponses[0].remindDay")
				.value(response.getMessageResponses().get(0).getRemindDay()),
			jsonPath("$.data.messageResponses[0].reminded")
				.value(response.getMessageResponses().get(0).isReminded())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-reminds-success")
				.tag(ApiTag.REMIND)
				.summary("리마인드 정보 보기 API")
				.description("리마인드 페이지에서 리마인드 정보들을 불러옵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void getRemindResponse_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		AjajaException tokenException = new AjajaException(errorCode);
		doThrow(tokenException).when(findPlanRemindQuery).findByUserIdAndPlanId(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(get(REMIND_END_POINT + "/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-reminds-fail-" + identifier)
				.tag(ApiTag.REMIND)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("만일 조회되는 계획이 없다면 예외를 던진다.")
	void getRemindResponse_Fail_ByPlanNotFound() throws Exception {
		// given
		AjajaException notFoundException = new AjajaException(NOT_FOUND_PLAN);
		doThrow(notFoundException).when(findPlanRemindQuery).findByUserIdAndPlanId(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(get(REMIND_END_POINT + "/{planId}", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-reminds-fail-plan-not-found")
				.tag(ApiTag.REMIND)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
