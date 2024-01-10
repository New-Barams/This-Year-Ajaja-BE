package com.newbarams.ajaja.module.plan.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.common.util.DocsGenerator;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

class PlanControllerTest extends WebMvcTestSupport {
	private static final List<String> TAG_FIXTURE = List.of("올해도", "아좌좌");
	private static final PlanResponse.Detail DETAIL_RESPONSE_FIXTURE = new PlanResponse.Detail(
		new PlanResponse.Writer("공부하는 돼지", true, true),
		1L, "올해도 아좌좌", "아좌좌 마이 라이프", 1, true, true, true, 15000,
		TAG_FIXTURE, Instant.now()
	);

	@ApiTest
	void updatePlan_Success() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);
		PlanResponse.Detail response = DETAIL_RESPONSE_FIXTURE;

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willReturn(response);

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer Token")
			.header("Month", 12)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data.writer.nickname").value(response.getWriter().getNickname()),
			jsonPath("$.data.writer.owner").value(response.getWriter().isOwner()),
			jsonPath("$.data.writer.ajajaPressed").value(response.getWriter().isAjajaPressed()),
			jsonPath("$.data.id").value(response.getId()),
			jsonPath("$.data.title").value(response.getTitle()),
			jsonPath("$.data.description").value(response.getDescription()),
			jsonPath("$.data.icon").value(response.getIcon()),
			jsonPath("$.data.public").value(response.isPublic()),
			jsonPath("$.data.canRemind").value(response.isCanRemind()),
			jsonPath("$.data.canAjaja").value(response.isCanAjaja()),
			jsonPath("$.data.ajajas").value(response.getAjajas()),
			jsonPath("$.data.tags[0]").value(response.getTags().get(0)),
			jsonPath("$.data.createdAt").value(String.valueOf(response.getCreatedAt()))
		);

		// docs
		result.andDo(
			DocsGenerator.generate(
				"계획 API",
				"계획 수정",
				"계획 수정 API",
				"요청한 데이터로 계획을 수정합니다.",
				true,
				result
			)
		);
	}

	@ApiTest
	void getAllPlans_Success() throws Exception {
		// given
		PlanRequest.GetAll request = sut.giveMeOne(PlanRequest.GetAll.class);
		List<PlanResponse.GetAll> response =
			List.of(new PlanResponse.GetAll(1L, 1L, "공부하는 돼지", "올해도 아좌좌", 1, 15000, TAG_FIXTURE, Instant.now()));

		given(getPlanService.loadAllPlans(request)).willReturn(response);

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.get(PLAN_END_POINT)
			.contentType(MediaType.APPLICATION_JSON)
			.param("sort", request.getSort())
			.param("current", String.valueOf(request.isCurrent()))
			.param("ajaja", String.valueOf(request.getAjaja()))
			.param("start", String.valueOf(request.getStart())));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data[0].id").value(response.get(0).getId()),
			jsonPath("$.data[0].userId").value(response.get(0).getUserId()),
			jsonPath("$.data[0].nickname").value(response.get(0).getNickname()),
			jsonPath("$.data[0].title").value(response.get(0).getTitle()),
			jsonPath("$.data[0].iconNumber").value(response.get(0).getIconNumber()),
			jsonPath("$.data[0].ajajas").value(response.get(0).getAjajas()),
			jsonPath("$.data[0].tags[0]").value(response.get(0).getTags().get(0)),
			jsonPath("$.data[0].createdAt").value(String.valueOf(response.get(0).getCreatedAt()))
		);

		// docs
		result.andDo(
			DocsGenerator.generate(
				"계획 API",
				"전체 계획 조회",
				"전체 계획 조회 API",
				"커서 기반으로 계획을 가져온다",
				false,
				result
			)
			// MockMvcRestDocumentationWrapper.document("전체 계획 조회",
			// 	Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
			// 	Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
			// 	ResourceDocumentation.resource(ResourceSnippetParameters.builder()
			// 		.tag("계획 API")
			// 		.summary("전체 계획 조회 API")
			// 		.description("")
			// 		.queryParameters(
			// 			ResourceDocumentation.parameterWithName("sort").description("정렬 대상"),
			// 			ResourceDocumentation.parameterWithName("current").description("올해 값인지 조회"),
			// 			ResourceDocumentation.parameterWithName("ajaja").description("이게 무슨 값이지?"),
			// 			ResourceDocumentation.parameterWithName("start").description("조회 번호")
			// 		)
			// 		.responseFields(
			// 			fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
			// 			fieldWithPath("data").type(JsonFieldType.ARRAY).description("계획 목록"),
			// 			fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("계획 번호"),
			// 			fieldWithPath("data[].userId").type(JsonFieldType.NUMBER).description("사용자 번호"),
			// 			fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
			// 			fieldWithPath("data[].title").type(JsonFieldType.STRING).description("계획 제목"),
			// 			fieldWithPath("data[].iconNumber").type(JsonFieldType.NUMBER).description("계획 아이콘 식별자"),
			// 			fieldWithPath("data[].ajajas").type(JsonFieldType.NUMBER).description("계획 좋아요 수"),
			// 			fieldWithPath("data[].tags.[]").type(JsonFieldType.ARRAY).description("계획 태그 배열"),
			// 			fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("계획 생성 시간")
			// 		)
			// 		.build()
			// 	)
			// )
		);
	}

}
