package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.common.util.ApiTag;
import com.newbarams.ajaja.common.util.RestDocument;

class LogoutControllerTest extends WebMvcTestSupport {

	@ApiTest
	void logout_Success() throws Exception {
		// given

		// when
		var result = mockMvc.perform(post(USER_END_POINT + "/logout")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isNoContent());

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("logout")
				.tag(ApiTag.USER)
				.summary("로그아웃 API")
				.description("발급된 사용자의 토큰을 만료시킵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
