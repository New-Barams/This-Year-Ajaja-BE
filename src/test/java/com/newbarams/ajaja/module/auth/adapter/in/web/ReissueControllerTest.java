package com.newbarams.ajaja.module.auth.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.module.auth.dto.AuthRequest;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;

class ReissueControllerTest extends WebMvcTestSupport {

	@ApiTest
	void reissue_Success() throws Exception {
		// given
		AuthRequest.Reissue request = sut.giveMeOne(AuthRequest.Reissue.class);
		AuthResponse.Token response = sut.giveMeOne(AuthResponse.Token.class);

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
			MockMvcRestDocumentationWrapper.document("reissue api",
				Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
				Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
				ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("인증 API")
					.summary("토큰 재발급 API")
					.description("기존에 발급받은 토큰으로 새로운 토큰을 재발급할 수 있습니다.")
					.requestFields(
						fieldWithPath("accessToken").type(JsonFieldType.STRING).description("엑세스 토큰"),
						fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
					)
					.responseFields(
						fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
						fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("엑세스 토큰"),
						fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
						fieldWithPath("data.accessTokenExpireIn").type(JsonFieldType.NUMBER).description("엑세스 토큰 만료시간")
					)
					.build()
				)
			)
		);
	}
}
