package com.newbarams.ajaja.module.auth.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.common.util.ApiTag;
import com.newbarams.ajaja.common.util.RestDocument;
import com.newbarams.ajaja.module.auth.dto.AuthRequest;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;

class LoginControllerTest extends WebMvcTestSupport {

	@ApiTest
	void login_Success() throws Exception {
		// given
		AuthRequest.Login request = new AuthRequest.Login(
			"3yZ1t-T6P0lmA51PDW0jJkKjyXazFBEKKwyoAAABi_tgGJZAPV-WDrAHcw",
			"http://localhost:3000/oauth"
		);

		AuthResponse.Token response = new AuthResponse.Token("accessToken", "refreshToken", 1703002695);

		given(loginUseCase.login(anyString(), anyString())).willReturn(response);

		// when
		var result = mockMvc.perform(post("/login")
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
				.identifier("login")
				.tag(ApiTag.AUTH)
				.summary("로그인 API")
				.description("인증 서버로부터 발급 받은 인가 코드로 로그인을 시도합니다.")
				.result(result)
				.generateDocs()
		);
	}
}
