package com.newbarams.ajaja.module.remind.adapter.in.web;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
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
import com.newbarams.ajaja.module.plan.dto.PlanRequest;

class UpdateRemindInfoControllerTest extends WebMvcTestSupport {
	private final List<PlanRequest.Message> messages = List.of(
		new PlanRequest.Message("화이팅", 3, 1),
		new PlanRequest.Message("잘하고 있지?", 6, 1),
		new PlanRequest.Message("아좌좌", 9, 1),
		new PlanRequest.Message("얼마 안남았다", 12, 1)
	);

	private final PlanRequest.UpdateRemind request =
		new PlanRequest.UpdateRemind(12, 3, 1, "MORNING", messages);

	@ApiTest
	@DisplayName("해당 계획의 리마인드 정보들을 수정한다.")
	void modifyRemindInfo_Success_WithNoException() throws Exception {
		// given
		doNothing().when(updateRemindInfoUseCase).update(anyLong(), anyLong(), any());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT + "/{planId}/reminds", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-my-plans")
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 정보 수정 API")
				.description("리마인드 정보들을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void modifyRemindInfo_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		AjajaException tokenException = new AjajaException(errorCode);
		doThrow(tokenException).when(updateRemindInfoUseCase).update(anyLong(), anyLong(), any());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT + "/{planId}/reminds", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-my-plans-invalid_token")
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 정보 수정 API")
				.description("리마인드 정보들을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("변경 가능한 기간이 아니라면 예외를 던진다.")
	void modifyRemindInfo_Fail_ByNotUpdatableDate() throws Exception {
		// given
		doNothing().when(updateRemindInfoUseCase).update(anyLong(), anyLong(), any());

		AjajaException notUpdatableException = new AjajaException(INVALID_UPDATABLE_DATE);
		doThrow(notUpdatableException).when(updateRemindInfoUseCase).update(anyLong(), anyLong(), any());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT + "/{planId}/reminds", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-my-plans-not-updatable-date")
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 정보 수정 API")
				.description("리마인드 정보들을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("만일 조회된 계획이 없다면 예외를 던진다.")
	void modifyRemindInfo_Fail_ByNotFoundPlan() throws Exception {
		// given
		doNothing().when(updateRemindInfoUseCase).update(anyLong(), anyLong(), any());

		AjajaException notFoundException = new AjajaException(NOT_FOUND_PLAN);
		doThrow(notFoundException).when(updateRemindInfoUseCase).update(anyLong(), anyLong(), any());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT + "/{planId}/reminds", 1)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-my-plans-not-found-plan")
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 정보 수정 API")
				.description("리마인드 정보들을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}