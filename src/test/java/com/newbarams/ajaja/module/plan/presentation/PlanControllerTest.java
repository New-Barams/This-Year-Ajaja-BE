package com.newbarams.ajaja.module.plan.presentation;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.annotation.ParameterizedApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.common.util.ApiTag;
import com.newbarams.ajaja.common.util.RestDocument;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.exception.ErrorCode;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

class PlanControllerTest extends WebMvcTestSupport {
	private static final List<String> TAG_FIXTURE = List.of("올해도", "아좌좌");
	private static final List<PlanRequest.Message> MESSAGE_FIXTURE = List.of(new PlanRequest.Message("content1", 6, 1),
		new PlanRequest.Message("content2", 12, 1));
	private static final PlanResponse.Detail DETAIL_RESPONSE_FIXTURE = new PlanResponse.Detail(
		new PlanResponse.Writer("공부하는 돼지", true, true),
		1L, "올해도 아좌좌", "아좌좌 마이 라이프", 1, true, true, true, 15000,
		TAG_FIXTURE, Instant.now()
	);

	@ApiTest
	void updatePlan_Success() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);
		PlanResponse.Detail response = DETAIL_RESPONSE_FIXTURE; // to reduce getter on validation

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willReturn(response);

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
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
			RestDocument.builder()
				.identifier("plan-update")
				.tag(ApiTag.PLAN)
				.summary("계획 수정 API")
				.description("요청한 데이터로 계획을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void updatePlan_Fail_By_Not_Found_Plan() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willThrow(
			new AjajaException(NOT_FOUND_PLAN));

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-update-notFoundPlan")
				.tag(ApiTag.PLAN)
				.summary("계획 수정 API")
				.description("요청한 데이터로 계획을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("수정 가능한 기간이 아닌 경우 400 에러를 반환한다.")
	void updatePlan_Fail_By_Not_Modifiable_Month() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willThrow(
			new AjajaException(INVALID_UPDATABLE_DATE));

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-update-notModifiableMonth")
				.tag(ApiTag.PLAN)
				.summary("계획 수정 API")
				.description("요청한 데이터로 계획을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void updatePlan_Fail_By_Invalid_User_Access() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willThrow(
			new AjajaException(INVALID_USER_ACCESS));

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-update-invalidUserAccess")
				.tag(ApiTag.PLAN)
				.summary("계획 수정 API")
				.description("요청한 데이터로 계획을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void updatePlan_Fail_By_Invalid_Token(ErrorCode errorCode, String identifier) throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt()))
			.willThrow(new AjajaException(errorCode));

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier(identifier)
				.tag(ApiTag.PLAN)
				.summary("계획 수정 API")
				.description("요청한 데이터로 계획을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
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
			RestDocument.builder()
				.identifier("plan-query-all")
				.tag(ApiTag.PLAN)
				.summary("전체 계획 조회 API")
				.description("커서 기반으로 계획을 가져온다")
				.result(result)
				.generateDocs()
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

	@ApiTest
	void deletePlan_Success() throws Exception {
		// given
		doNothing().when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete")
				.tag(ApiTag.PLAN)
				.summary("계획 삭제 API")
				.description("해당 ID의 계획을 삭제합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void deletePlan_Fail_By_Not_Found_Plan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN)).when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete-notFoundPlan")
				.tag(ApiTag.PLAN)
				.summary("계획 삭제 API")
				.description("해당 ID의 계획을 삭제합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("삭제 가능한 기간이 아닌 경우 400 에러를 반환한다.")
	void deletePlan_Fail_By_Not_Modifiable_Month() throws Exception {
		// given
		doThrow(new AjajaException(INVALID_UPDATABLE_DATE))
			.when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete-notModifiableMonth")
				.tag(ApiTag.PLAN)
				.summary("계획 삭제 API")
				.description("해당 ID의 계획을 삭제합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void deletePlan_Fail_By_Invalid_User_Access() throws Exception {
		// given
		doThrow(new AjajaException(INVALID_USER_ACCESS)).when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete-invalidUserAccess")
				.tag(ApiTag.PLAN)
				.summary("계획 삭제 API")
				.description("해당 ID의 계획을 삭제합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void deletePlan_Fail_By_Invalid_Token(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode)).when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier(identifier)
				.tag(ApiTag.PLAN)
				.summary("계획 삭제 API")
				.description("해당 ID의 계획을 삭제합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void getPlan_Success() throws Exception {
		// given
		PlanResponse.Detail response = DETAIL_RESPONSE_FIXTURE;

		given(getPlanService.loadByIdAndOptionalUser(anyLong(), anyLong())).willReturn(response);

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.get(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

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
			RestDocument.builder()
				.identifier("plan-get")
				.tag(ApiTag.PLAN)
				.summary("계획 단건 조회 API")
				.description("해당 ID의 계획을 조회합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void getPlan_Fail_By_Invalid_Token(ErrorCode errorCode, String identifier) throws Exception {
		// given
		given(getPlanService.loadByIdAndOptionalUser(anyLong(), anyLong()))
			.willThrow(new AjajaException(errorCode));

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.get(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier(identifier)
				.tag(ApiTag.PLAN)
				.summary("계획 단건 조회 API")
				.description("해당 ID의 계획을 조회합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void createPlan_Success() throws Exception {
		// given
		PlanRequest.Create request = new PlanRequest.Create("올해도 아좌좌", "아좌좌 마이 라이프", 12, 6, 1, "MORNING", true, true, 1,
			TAG_FIXTURE, MESSAGE_FIXTURE);
		PlanResponse.Create response = new PlanResponse.Create(1L, 1L, "올해도 아좌좌", "아좌좌 마이 라이프", 1, true, true, true,
			TAG_FIXTURE);

		given(createPlanService.create(anyLong(), any(), anyInt())).willReturn(response);

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post(PLAN_END_POINT)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isCreated(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data.id").value(response.getId()),
			jsonPath("$.data.userId").value(response.getUserId()),
			jsonPath("$.data.title").value(response.getTitle()),
			jsonPath("$.data.description").value(response.getDescription()),
			jsonPath("$.data.iconNumber").value(response.getIconNumber()),
			jsonPath("$.data.public").value(response.isPublic()),
			jsonPath("$.data.canRemind").value(response.isCanRemind()),
			jsonPath("$.data.canAjaja").value(response.isCanAjaja()),
			jsonPath("$.data.ajajas").value(response.getAjajas()),
			jsonPath("$.data.tags[0]").value(response.getTags().get(0))
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-create")
				.tag(ApiTag.PLAN)
				.summary("계획 생성 API")
				.description("요청한 데이터로 계획을 생성합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("생성 가능한 기간이 아닌 경우 400 에러를 반환한다.")
	void createPlan_Fail_By_Not_Creatable_Month() throws Exception {
		// given
		PlanRequest.Create request = new PlanRequest.Create("올해도 아좌좌", "아좌좌 마이 라이프", 12, 6, 1, "MORNING", true, true, 1,
			TAG_FIXTURE, MESSAGE_FIXTURE);

		given(createPlanService.create(anyLong(), any(), anyInt()))
			.willThrow(new AjajaException(INVALID_UPDATABLE_DATE));

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post(PLAN_END_POINT)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-create-notCreatableMonth")
				.tag(ApiTag.PLAN)
				.summary("계획 생성 API")
				.description("요청한 데이터로 계획을 생성합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void createPlan_Fail_By_Invalid_Token(ErrorCode errorCode, String identifier) throws Exception {
		// given
		PlanRequest.Create request = new PlanRequest.Create("올해도 아좌좌", "아좌좌 마이 라이프", 12, 6, 1, "MORNING", true, true, 1,
			TAG_FIXTURE, MESSAGE_FIXTURE);

		given(createPlanService.create(anyLong(), any(), anyInt())).willThrow(new AjajaException(errorCode));

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post(PLAN_END_POINT)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier(identifier)
				.tag(ApiTag.PLAN)
				.summary("계획 생성 API")
				.description("요청한 데이터로 계획을 생성합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void updatePlanPublicStatus_Success() throws Exception {
		// given
		doNothing().when(updatePlanService).updatePublicStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/public"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planPublicStatus-update")
				.tag(ApiTag.PLAN)
				.summary("계획 공개 여부 변경 API")
				.description("계획의 공개 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void updatePlanPublicStatus_Fail_By_Not_Found_Plan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN)).when(updatePlanService).updatePublicStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/public"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planPublicStatus-update-notFoundPlan")
				.tag(ApiTag.PLAN)
				.summary("계획 공개 여부 변경 API")
				.description("계획의 공개 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void updatePlanPublicStatus_Fail_By_Invalid_User_Access() throws Exception {
		// given
		doThrow(new AjajaException(INVALID_USER_ACCESS))
			.when(updatePlanService).updatePublicStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/public"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planPublicStatus-update-invalidUserAccess")
				.tag(ApiTag.PLAN)
				.summary("계획 공개 여부 변경 API")
				.description("계획의 공개 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void updatePlanPublicStatus_Fail_By_Invalid_Token(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode)).when(updatePlanService).updatePublicStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/public"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier(identifier)
				.tag(ApiTag.PLAN)
				.summary("계획 공개 여부 변경 API")
				.description("계획의 공개 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void updatePlanRemindStatus_Success() throws Exception {
		// given
		doNothing().when(updatePlanService).updateRemindStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/remindable"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planRemindStatus-update")
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 알림 여부 변경 API")
				.description("계획의 리마인드 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void updatePlanRemindStatus_Fail_By_Not_Found_Plan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN)).when(updatePlanService).updateRemindStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/remindable"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planRemindStatus-update-notFoundPlan")
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 알림 여부 변경 API")
				.description("계획의 리마인드 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void updatePlanRemindStatus_Fail_By_Invalid_User_Access() throws Exception {
		// given
		doThrow(new AjajaException(INVALID_USER_ACCESS))
			.when(updatePlanService).updateRemindStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/remindable"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planRemindStatus-update-invalidUserAccess")
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 알림 여부 변경 API")
				.description("계획의 리마인드 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void updatePlanRemindStatus_Fail_By_Invalid_Token(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode))
			.when(updatePlanService).updateRemindStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/remindable"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier(identifier)
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 알림 여부 변경 API")
				.description("계획의 리마인드 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void updatePlanAjajaStatus_Success() throws Exception {
		// given
		doNothing().when(updatePlanService).updateAjajaStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planAjajaStatus-update")
				.tag(ApiTag.PLAN)
				.summary("계획 응원 메시지 알림 여부 변경 API")
				.description("계획의 응원 메시지 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void updatePlanAjajaStatus_Fail_Not_Found_Plan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN))
			.when(updatePlanService).updateAjajaStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planAjajaStatus-update-notFoundPlan")
				.tag(ApiTag.PLAN)
				.summary("계획 응원 메시지 알림 여부 변경 API")
				.description("계획의 응원 메시지 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void updatePlanAjajaStatus_Fail_By_Invalid_User_Access() throws Exception {
		// given
		doThrow(new AjajaException(INVALID_USER_ACCESS))
			.when(updatePlanService).updateAjajaStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("planAjajaStatus-update-invalidUserAccess")
				.tag(ApiTag.PLAN)
				.summary("계획 응원 메시지 알림 여부 변경 API")
				.description("계획의 응원 메시지 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void updatePlanAjajaStatus_Fail_By_Invalid_Token(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode))
			.when(updatePlanService).updateAjajaStatus(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.put(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier(identifier)
				.tag(ApiTag.PLAN)
				.summary("계획 응원 메시지 알림 여부 변경 API")
				.description("계획의 응원 메시지 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	void switchAjaja_Success() throws Exception {
		// given
		doNothing().when(switchAjajaService).switchOrAddIfNotExist(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("ajaja-switch")
				.tag(ApiTag.PLAN)
				.summary("아좌좌 추가 or 취소 API")
				.description("유저의 아좌좌 누름 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void switchAjaja_Fail_By_Not_Found_Plan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN))
			.when(switchAjajaService).switchOrAddIfNotExist(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("ajaja-switch-notFoundPlan")
				.tag(ApiTag.PLAN)
				.summary("아좌좌 추가 or 취소 API")
				.description("유저의 아좌좌 누름 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void switchAjaja_Fail_By_Invalid_Token(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode))
			.when(switchAjajaService).switchOrAddIfNotExist(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier(identifier)
				.tag(ApiTag.PLAN)
				.summary("아좌좌 추가 or 취소 API")
				.description("유저의 아좌좌 누름 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
