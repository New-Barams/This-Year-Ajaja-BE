package me.ajaja.module.footprint.adapter.in.web;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpHeaders;

import jakarta.validation.ConstraintViolationException;
import me.ajaja.common.annotation.ApiTest;
import me.ajaja.common.support.WebMvcTestSupport;
import me.ajaja.common.util.ApiTag;
import me.ajaja.common.util.RestDocument;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.dto.FootprintRequest;

class CreateFootprintControllerTest extends WebMvcTestSupport {

	@ApiTest
	@DisplayName("발자취 유형에 맞는 형식에 요청 값에 대해 발자취 생성에 성공한다.")
	void createFootprint_Success_With_NoExceptions() throws Exception {
		// given
		FootprintRequest.Create request = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("param.type", Footprint.Type.FREE)
			.set("param.content", "contents")
			.set("tags", List.of("tag1", "tag2"))
			.sample();

		doNothing().when(createFootprintUseCase).create(anyLong(), any());

		// when
		var result = mockMvc.perform(post(FOOTPRINT_END_POINT)
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

	@ApiTest
	@DisplayName("발자취 유형에 해당 하는 항목 값이 빈 문자일 때 대해 발자취 생성에 실패한다.")
	void createFootprint_Fail_By_InvalidContents() throws Exception {
		// given
		FootprintRequest.Create request = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("param.content", "")
			.set("tags", List.of("tag1", "tag2"))
			.sample();

		doThrow(ConstraintViolationException.class).when(createFootprintUseCase)
			.create(anyLong(), any());

		// when
		var result = mockMvc.perform(post(FOOTPRINT_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.accept(APPLICATION_JSON)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		result.andExpectAll(
			status().isBadRequest(),
			jsonPath("$.httpStatus").value(BAD_REQUEST.name()),
			jsonPath("$.errorName").value(BEAN_VALIDATION_FAIL_EXCEPTION.name()),
			jsonPath("$.errorMessage").value(BEAN_VALIDATION_FAIL_EXCEPTION.getMessage())
		);

		// docs
		result.andDo(RestDocument.builder()
			.identifier("footprint-create-fail-invalid-type-contents")
			.tag(ApiTag.FOOTPRINT)
			.secured(true)
			.result(result)
			.generateDocs());
	}
}
