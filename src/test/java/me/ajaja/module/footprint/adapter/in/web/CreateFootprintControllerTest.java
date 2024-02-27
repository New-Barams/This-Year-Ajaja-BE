package me.ajaja.module.footprint.adapter.in.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import jakarta.validation.ConstraintViolationException;
import me.ajaja.common.annotation.ApiTest;
import me.ajaja.common.support.WebMvcTestSupport;
import me.ajaja.common.util.ApiTag;
import me.ajaja.common.util.RestDocument;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.dto.FootprintParam;
import me.ajaja.module.footprint.dto.FootprintRequest;

class CreateFootprintControllerTest extends WebMvcTestSupport {
	@Test
	@ApiTest
	@DisplayName("발자취 유영에 맞는 형식에 요청 값에 대해 발자취 생성에 성공한다.")
	void createFootprint_Success_With_NoExceptions() throws Exception {
		// given
		FootprintRequest.Create request = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("param.type", Footprint.Type.FREE)
			.set("param.content", "contents")
			.sample();

		Long userId = 1L;
		Long createFootprintId = 1L;

		when(createFootprintUseCase.create(anyLong(), anyLong(), any(FootprintParam.Create.class))).thenReturn(
			createFootprintId);

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post("/targets/{id}/footprints", userId)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.accept(APPLICATION_JSON)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isCreated());

		// docs
		result.andDo(RestDocument.builder()
			.identifier("footprint-create-success")
			.tag(ApiTag.FOOTPRINT)
			.summary("발자취 생성 API")
			.description("발자취 유형에 맞는 항목에 빈 문자가 아닌 상태로 요청할 떄 발자취 정보를 생성 합니다.")
			.secured(true)
			.result(result)
			.generateDocs());
	}

	@Test
	@DisplayName("발자취 유형에 해당 하는 항목 값이 빈 문자일 때 대해 발자취 생성에 실패한다.")
	void createFootprint_Fail_By_InvalidContents() throws Exception {
		// given
		Long userId = 1L;
		FootprintRequest.Create request = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("param.content", "")
			.sample();

		doThrow(ConstraintViolationException.class).when(createFootprintUseCase)
			.create(anyLong(), anyLong(), any(FootprintParam.Create.class));

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post("/targets/{id}/footprints", userId)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.accept(APPLICATION_JSON)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		result.andExpect(status().is5xxServerError());

		// docs
		result.andDo(RestDocument.builder()
			.identifier("footprint-create-fail-invalid-type-contents")
			.tag(ApiTag.FOOTPRINT)
			.summary("발자취 생성 API")
			.description("발자취 유형에 해당 하는 항목 값이 빈 문자일 때 대해 발자취 생성에 실패 합니다")
			.secured(true)
			.result(result)
			.generateDocs());
	}
}
