package me.ajaja.module.plan.presentation;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
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
import me.ajaja.module.plan.dto.BanWordValidationResult;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

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
	@DisplayName("[계획 수정] 요청이 들어오면 요청된 데이터로 계획이 수정된다.")
	void updatePlan_Success() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);
		PlanResponse.Detail response = DETAIL_RESPONSE_FIXTURE; // to reduce getter on validation

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willReturn(response);

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}"), 1)
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
				.identifier("plan-update-success")
				.tag(ApiTag.PLAN)
				.summary("계획 수정 API")
				.description("요청한 데이터로 계획을 수정합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 수정] 플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void updatePlan_Fail_ByNotFoundPlan() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willThrow(
			new AjajaException(NOT_FOUND_PLAN));

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-update-fail-plan-not-found")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 수정] 수정 가능한 기간이 아닌 경우 400 에러를 반환한다.")
	void updatePlan_Fail_ByNotModifiableMonth() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willThrow(
			new AjajaException(UNMODIFIABLE_DURATION));

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-update-fail-not-modifiable-month")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 수정] 잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void updatePlan_Fail_ByInvalidUserAccess() throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willThrow(
			new AjajaException(NOT_AUTHOR));

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-update-fail-invalid-user-Access")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("[계획 수정] 토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void updatePlan_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		PlanRequest.Update request = new PlanRequest.Update(1, "올해도 아좌좌", "아좌좌 마이 라이프", true, true, TAG_FIXTURE);

		given(updatePlanService.update(anyLong(), anyLong(), any(), anyInt())).willThrow(new AjajaException(errorCode));

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-update-fail-" + identifier)
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 전체 조회] 요청이 들어오면 전체 계획을 조회할 수 있다.")
	void getAllPlans_Success() throws Exception {
		// given
		PlanRequest.GetAll request = new PlanRequest.GetAll("latest", true, 1, 1L);
		List<PlanResponse.GetAll> response =
			List.of(new PlanResponse.GetAll(1L, 1L, "공부하는 돼지", "올해도 아좌좌", 1, 15000, TAG_FIXTURE, Instant.now()));

		given(findAllPlansQuery.findAllByCursorAndSorting(request)).willReturn(response);

		// when
		var result = mockMvc.perform(get(PLAN_END_POINT)
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
				.identifier("plan-query-all-success")
				.tag(ApiTag.PLAN)
				.summary("전체 계획 조회 API")
				.description("커서 기반으로 계획을 가져온다 <br>") // todo: 상세하게 적어주기
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
	@DisplayName("[계획 삭제] 요청이 들어오면 계획이 삭제된다.")
	void deletePlan_Success() throws Exception {
		// given
		doNothing().when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete-success")
				.tag(ApiTag.PLAN)
				.summary("계획 삭제 API")
				.description("해당 ID의 계획을 삭제합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 삭제] 플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void deletePlan_Fail_ByNotFoundPlan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN)).when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete-fail-not-found")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 삭제] 삭제 가능한 기간이 아닌 경우 400 에러를 반환한다.")
	void deletePlan_Fail_ByNotModifiableMonth() throws Exception {
		// given
		AjajaException unmodifiableDuration = new AjajaException(UNMODIFIABLE_DURATION);
		doThrow(unmodifiableDuration).when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete-fail-not-modifiable-month")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 삭제] 잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void deletePlan_Fail_ByInvalidUserAccess() throws Exception {
		// given
		doThrow(new AjajaException(NOT_AUTHOR)).when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete-fail-invalid-user-access")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("[계획 삭제] 토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void deletePlan_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode)).when(deletePlanService).delete(anyLong(), anyLong(), anyInt());

		// when
		var result = mockMvc.perform(delete(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-delete-fail-" + identifier)
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 단건 조회] 요청이 들어오면 계획 정보를 조회할 수 있다.")
	void getPlan_Success() throws Exception {
		// given
		PlanResponse.Detail response = DETAIL_RESPONSE_FIXTURE;

		given(loadPlanDetailUseCase.loadByIdAndOptionalUser(anyLong(), anyLong())).willReturn(response);

		// when
		var result = mockMvc.perform(get(PLAN_END_POINT.concat("/{id}"), 1)
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
				.identifier("plan-get-one-success")
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
	@DisplayName("[계획 단건 조회] 토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void getPlan_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		given(loadPlanDetailUseCase.loadByIdAndOptionalUser(anyLong(), anyLong()))
			.willThrow(new AjajaException(errorCode));

		// when
		var result = mockMvc.perform(get(PLAN_END_POINT.concat("/{id}"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-get-one-fail-" + identifier)
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 생성] 요청된 데이터로 계획을 생성한다.")
	void createPlan_Success() throws Exception {
		// given
		PlanRequest.Create request = new PlanRequest.Create("올해도 아좌좌", "아좌좌 마이 라이프", 12, 6, 1, "MORNING", true, true, 1,
			TAG_FIXTURE, MESSAGE_FIXTURE);

		given(createPlanService.create(anyLong(), any(), anyInt())).willReturn(1L);

		// when
		var result = mockMvc.perform(post(PLAN_END_POINT)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		String expectedPath = "plans/1";

		// then
		result.andExpectAll(
			status().isCreated(),
			header().string(HttpHeaders.LOCATION, expectedPath)
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-create-success")
				.tag(ApiTag.PLAN)
				.summary("계획 생성 API")
				.description("요청한 데이터로 계획을 생성합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 생성] 생성 가능한 기간이 아닌 경우 400 에러를 반환한다.")
	void createPlan_Fail_ByNotCreatableMonth() throws Exception {
		// given
		PlanRequest.Create request = new PlanRequest.Create("올해도 아좌좌", "아좌좌 마이 라이프", 12, 6, 1, "MORNING", true, true, 1,
			TAG_FIXTURE, MESSAGE_FIXTURE);

		AjajaException unmodifiableDuration = new AjajaException(UNMODIFIABLE_DURATION);
		willThrow(unmodifiableDuration).given(createPlanService).create(anyLong(), any(), anyInt());

		// when
		var result = mockMvc.perform(post(PLAN_END_POINT)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-create-fail-not-creatable-month")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("[계획 생성] 토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void createPlan_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		PlanRequest.Create request = new PlanRequest.Create("올해도 아좌좌", "아좌좌 마이 라이프", 12, 6, 1, "MORNING", true, true, 1,
			TAG_FIXTURE, MESSAGE_FIXTURE);

		given(createPlanService.create(anyLong(), any(), anyInt())).willThrow(new AjajaException(errorCode));

		// when
		var result = mockMvc.perform(post(PLAN_END_POINT)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.header("Month", 1)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-create-fail-" + identifier)
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 공개 여부 변경] 요청이 들어오면 계획의 공개 여부가 변경된다.")
	void updatePlanPublicStatus_Success() throws Exception {
		// given
		doNothing().when(switchPlanStatusUseCase).switchPublic(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/public"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-public-success")
				.tag(ApiTag.PLAN)
				.summary("계획 공개 여부 변경 API")
				.description("계획의 공개 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 공개 여부 변경] 플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void updatePlanPublicStatus_Fail_ByNotFoundPlan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN))
			.when(switchPlanStatusUseCase).switchPublic(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/public"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-public-fail-not-found")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 공개 여부 변경] 잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void updatePlanPublicStatus_Fail_ByInvalidUserAccess() throws Exception {
		// given
		doThrow(new AjajaException(NOT_AUTHOR)).when(switchPlanStatusUseCase).switchPublic(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/public"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-public-fail-invalid-user-access")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("[계획 공개 여부 변경] 토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void updatePlanPublicStatus_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode)).when(switchPlanStatusUseCase).switchPublic(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/public"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-public-fail-" + identifier)
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 리마인드 알림 여부 변경] 요청이 들어오면 리마인드 알림 여부가 변경된다.")
	void updatePlanRemindStatus_Success() throws Exception {
		// given
		doNothing().when(switchPlanStatusUseCase).switchRemindable(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/remindable"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-remindable-success")
				.tag(ApiTag.PLAN)
				.summary("계획 리마인드 알림 여부 변경 API")
				.description("계획의 리마인드 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 리마인드 알림 여부 변경] 플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void updatePlanRemindStatus_Fail_ByNotFoundPlan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN))
			.when(switchPlanStatusUseCase).switchRemindable(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/remindable"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-remindable-fail-not-found")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[계획 리마인드 알림 여부 변경] 잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void updatePlanRemindStatus_Fail_ByInvalidUserAccess() throws Exception {
		// given
		doThrow(new AjajaException(NOT_AUTHOR)).when(switchPlanStatusUseCase).switchRemindable(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/remindable"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-remindable-fail-invalid-user-access")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("[계획 리마인드 알림 여부 변경] 토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void updatePlanRemindStatus_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode)).when(switchPlanStatusUseCase).switchRemindable(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/remindable"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-remindable-fail-" + identifier)
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[응원 메시지 알림 여부 변경] 요청이 들어오면 응원 메시지 알림 여부가 변경된다.")
	void updatePlanAjajaStatus_Success() throws Exception {
		// given
		doNothing().when(switchPlanStatusUseCase).switchAjajable(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-ajajable-success")
				.tag(ApiTag.PLAN)
				.summary("계획 응원 메시지 알림 여부 변경 API")
				.description("계획의 응원 메시지 알림 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[응원 메시지 알림 여부 변경] 플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void updatePlanAjajaStatus_Fail_ByNotFoundPlan() throws Exception {
		// given
		doThrow(new AjajaException(NOT_FOUND_PLAN))
			.when(switchPlanStatusUseCase).switchAjajable(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-ajajable-fail-not-found")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[응원 메시지 알림 여부 변경] 잘못된 유저의 접근일 경우 400 에러를 반환한다.")
	void updatePlanAjajaStatus_Fail_ByInvalidUserAccess() throws Exception {
		// given
		doThrow(new AjajaException(NOT_AUTHOR)).when(switchPlanStatusUseCase).switchAjajable(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-ajajable-fail-invalid-user-access")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("[응원 메시지 알림 여부 변경] 토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void updatePlanAjajaStatus_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode)).when(switchPlanStatusUseCase).switchAjajable(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(put(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("plan-to-ajajable-fail-" + identifier)
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[아좌좌 추가 or 취소] 요청이 들어오면 아좌좌 누름 여부가 변경된다.")
	void switchAjaja_Success() throws Exception {
		// given
		doNothing().when(switchAjajaService).switchOrAddIfNotExist(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(post(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("switch-ajaja-success")
				.tag(ApiTag.PLAN)
				.summary("아좌좌 추가 or 취소 API")
				.description("유저의 아좌좌 누름 여부를 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[아좌좌 추가 or 취소] 플랜이 존재하지 않을 경우 404 에러를 반환한다.")
	void switchAjaja_Fail_ByNotFoundPlan() throws Exception {
		// given
		AjajaException notFound = new AjajaException(NOT_FOUND_PLAN);
		willThrow(notFound).given(switchAjajaService).switchOrAddIfNotExist(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(post(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isNotFound());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("switch-ajaja-fail-not-found")
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("[아좌좌 추가 or 취소] 토큰 검증에 실패할 경우 400 에러를 반환한다.")
	void switchAjaja_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		doThrow(new AjajaException(errorCode)).when(switchAjajaService).switchOrAddIfNotExist(anyLong(), anyLong());

		// when
		var result = mockMvc.perform(post(PLAN_END_POINT.concat("/{id}/ajaja"), 1)
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

		// then
		result.andExpect(status().isBadRequest());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("switch-ajaja-fail-" + identifier)
				.tag(ApiTag.PLAN)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("[비속어 검증] 요청된 데이터에 대한 검증을 수행한다.")
	void validateContent_Success() throws Exception {
		// given
		PlanRequest.CheckBanWord request = new PlanRequest.CheckBanWord("title", "description");

		BanWordValidationResult.Common titleResult = new BanWordValidationResult.Common(true, List.of("tit"));
		BanWordValidationResult.Common descriptionResult = new BanWordValidationResult.Common(true, List.of("des"));
		BanWordValidationResult response = new BanWordValidationResult(titleResult, descriptionResult);

		given(validateContentUseCase.check(request)).willReturn(response);

		// when
		var result = mockMvc.perform(post(PLAN_END_POINT.concat("/validate"))
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data.title.existBanWord").value(Boolean.TRUE),
			jsonPath("$.data.title.banWords[0]").value("tit"),
			jsonPath("$.data.description.existBanWord").value(Boolean.TRUE),
			jsonPath("$.data.description.banWords[0]").value("des")
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("content-validate-success")
				.tag(ApiTag.PLAN)
				.summary("비속어 검증 API")
				.description("요청한 데이터로 비속어를 검증합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
