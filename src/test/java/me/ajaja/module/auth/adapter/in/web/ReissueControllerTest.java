package me.ajaja.module.auth.adapter.in.web;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import me.ajaja.common.annotation.ApiTest;
import me.ajaja.common.annotation.ParameterizedApiTest;
import me.ajaja.common.support.WebMvcTestSupport;
import me.ajaja.common.util.ApiTag;
import me.ajaja.common.util.RestDocument;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.module.auth.dto.AuthRequest;
import me.ajaja.module.auth.dto.AuthResponse;

class ReissueControllerTest extends WebMvcTestSupport {
	private final AuthRequest.Reissue request = new AuthRequest.Reissue("accessToken", "refreshToken");

	@ApiTest
	@DisplayName("유효한 토큰으로 토큰 재발급 요청하면 성공한다.")
	void reissue_Success() throws Exception {
		// given
		AuthResponse.Token response = new AuthResponse.Token("accessToken", "refreshToken", 1703002695);

		given(reissueTokenUseCase.reissue(anyString(), anyString())).willReturn(response);

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post("/reissue")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data.accessToken").value(response.getAccessToken()),
			jsonPath("$.data.refreshToken").value(response.getRefreshToken()),
			jsonPath("$.data.accessTokenExpireIn").value(response.getAccessTokenExpireIn())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("token-reissue-success")
				.tag(ApiTag.AUTH)
				.summary("토큰 재발급 API")
				.description("기존에 발급받은 토큰으로 새로운 토큰을 재발급할 수 있습니다.")
				.result(result)
				.generateDocs()
			// MockMvcRestDocumentationWrapper.document("reissue api",
			// 	Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
			// 	Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
			// 	ResourceDocumentation.resource(ResourceSnippetParameters.builder()
			// 		.tag("인증 API")
			// 		.summary("토큰 재발급 API")
			// 		.description("기존에 발급받은 토큰으로 새로운 토큰을 재발급할 수 있습니다.")
			// 		.requestFields(
			// 			fieldWithPath("accessToken").type(JsonFieldType.STRING).description("엑세스 토큰"),
			// 			fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
			// 		)
			// 		.responseFields(
			// 			fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
			// 			fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("엑세스 토큰"),
			// 			fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
			// 			fieldWithPath("data.accessTokenExpireIn").type(JsonFieldType.NUMBER).description("엑세스 토큰 만료시간")
			// 		)
			// 		.build()
			// 	)
			// )
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("토큰 검증에 실패하면 400에러를 반환한다.")
	void reissue_Fail_ByInvalidToken(ErrorCode errorCode, String identifier) throws Exception {
		// given
		RuntimeException invalidToken = new AjajaException(errorCode);

		willThrow(invalidToken).given(reissueTokenUseCase).reissue(anyString(), anyString());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post("/reissue")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

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
				.identifier("token-reissue-fail-" + identifier)
				.tag(ApiTag.AUTH)
				.result(result)
				.generateDocs()
		);
	}

	@ApiTest
	@DisplayName("로그인한 이력이 존재하지 않으면 404에러를 반환한다.")
	void reissue_Fail_ByNeverLogin() throws Exception {
		// given
		RuntimeException neverLogin = new AjajaException(NEVER_LOGIN);

		willThrow(neverLogin).given(reissueTokenUseCase).reissue(anyString(), anyString());

		// when
		var result = mockMvc.perform(RestDocumentationRequestBuilders.post("/reissue")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isNotFound(),
			jsonPath("$.httpStatus").value(NOT_FOUND.name()),
			jsonPath("$.errorName").value(NEVER_LOGIN.name()),
			jsonPath("$.errorMessage").value(NEVER_LOGIN.getMessage())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("token-reissue-fail-never-login")
				.tag(ApiTag.AUTH)
				.result(result)
				.generateDocs()
		);
	}
}
