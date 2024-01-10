package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.common.util.ApiTag;
import com.newbarams.ajaja.common.util.RestDocument;
import com.newbarams.ajaja.module.user.dto.UserResponse;

class GetMyPageControllerTest extends WebMvcTestSupport {

	@ApiTest
	void getMyPage_Success() throws Exception {
		// given
		UserResponse.MyPage response
			= new UserResponse.MyPage("공부하는 돼지", "ajaja@me.com", "ajaja@me.com", true, "KAKAO");

		given(getMyPagePort.findUserInfoById(anyLong())).willReturn(response);

		// when
		var result = mockMvc.perform(get(USER_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
			status().isOk(),
			jsonPath("$.success").value(Boolean.TRUE),
			jsonPath("$.data.nickname").value(response.getNickname()),
			jsonPath("$.data.defaultEmail").value(response.getDefaultEmail()),
			jsonPath("$.data.remindEmail").value(response.getRemindEmail()),
			jsonPath("$.data.emailVerified").value(response.isEmailVerified()),
			jsonPath("$.data.receiveType").value(response.getReceiveType())
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("get-my-page")
				.tag(ApiTag.USER)
				.summary("마이페이지 API")
				.description("마이페이지에서 사용될 사용자의 정보를 불러옵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
