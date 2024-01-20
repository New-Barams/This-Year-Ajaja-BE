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
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

class GetPlanInfoControllerTest extends WebMvcTestSupport {

	@ApiTest
	void getPlanInfo_Success() throws Exception {
		// given
		List<PlanResponse.PlanInfo> infos = List.of(
			new PlanResponse.PlanInfo(2023, 1L, "이력서 완성", true, 50, 1),
			new PlanResponse.PlanInfo(2023, 2L, "매일 운동하기", true, 0, 2)
		);
		List<PlanResponse.MainInfo> response = List.of(new PlanResponse.MainInfo(2023, 25, infos));

		given(getPlanInfoUseCase.load(anyLong())).willReturn(response);

		// when
		var result = mockMvc.perform(get(PLAN_END_POINT + "/main")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data[0].year").value(response.get(0).getYear()),
			jsonPath("$.data[0].totalAchieveRate").value(response.get(0).getTotalAchieveRate()),
			jsonPath("$.data[0].getPlanList[0].year").value(response.get(0).getGetPlanList().get(0).getYear()),
			jsonPath("$.data[0].getPlanList[0].planId").value(response.get(0).getGetPlanList().get(0).getPlanId()),
			jsonPath("$.data[0].getPlanList[0].title").value(response.get(0).getGetPlanList().get(0).getTitle()),
			jsonPath("$.data[0].getPlanList[0].remindable")
				.value(response.get(0).getGetPlanList().get(0).isRemindable()),
			jsonPath("$.data[0].getPlanList[0].achieveRate")
				.value(response.get(0).getGetPlanList().get(0).getAchieveRate()),
			jsonPath("$.data[0].getPlanList[0].icon").value(response.get(0).getGetPlanList().get(0).getIcon()),
			jsonPath("$.data[0].getPlanList[1].year").value(response.get(0).getGetPlanList().get(1).getYear()),
			jsonPath("$.data[0].getPlanList[1].planId").value(response.get(0).getGetPlanList().get(1).getPlanId()),
			jsonPath("$.data[0].getPlanList[1].title")
				.value(response.get(0).getGetPlanList().get(1).getTitle()),
			jsonPath("$.data[0].getPlanList[1].remindable")
				.value(response.get(0).getGetPlanList().get(1).isRemindable()),
			jsonPath("$.data[0].getPlanList[1].achieveRate")
				.value(response.get(0).getGetPlanList().get(1).getAchieveRate()),
			jsonPath("$.data[0].getPlanList[1].icon").value(response.get(0).getGetPlanList().get(1).getIcon())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-my-all-plans")
				.tag(ApiTag.PLAN)
				.summary("내 계획 전체보기 API")
				.description("메인페이지에서 사용자의 계획 정보들을 불러옵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void getPlanInfo_Fail_InvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		AjajaException tokenException = new AjajaException(errorCode);
		when(getPlanInfoUseCase.load(anyLong())).thenThrow(tokenException);

		// when
		var result = mockMvc.perform(get(PLAN_END_POINT + "/main")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-my-plans-invalid-token")
				.tag(ApiTag.PLAN)
				.summary("내 계획 전체보기 API")
				.description("메인페이지에서 사용자의 계획 정보들을 불러옵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void getPlanInfo_Fail_UserNotFound() throws Exception {
		// given
		AjajaException notFoundException = new AjajaException(USER_NOT_FOUND);
		when(getPlanInfoUseCase.load(anyLong())).thenThrow(notFoundException);

		// when
		var result = mockMvc.perform(get(PLAN_END_POINT + "/main")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().is4xxClientError());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-my-plans-user-not-found")
				.tag(ApiTag.PLAN)
				.summary("내 계획 전체보기 API")
				.description("메인페이지에서 사용자의 계획 정보들을 불러옵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
