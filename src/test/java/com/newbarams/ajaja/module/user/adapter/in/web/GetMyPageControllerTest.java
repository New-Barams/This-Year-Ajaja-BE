package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.annotation.ParameterizedApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.common.util.ApiTag;
import com.newbarams.ajaja.common.util.RestDocument;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.exception.ErrorCode;
import com.newbarams.ajaja.module.user.dto.UserResponse;

class GetMyPageControllerTest extends WebMvcTestSupport {

	@ApiTest
	@DisplayName("유효한 토큰으로 마이페이지 조회 요청을 보내면 성공한다.")
	void getMyPage_Success() throws Exception {
		// given
		UserResponse.MyPage response
			= new UserResponse.MyPage("공부하는 돼지", "ajaja@me.com", "ajaja@me.com", true, "KAKAO");

		given(getMyPageQuery.findUserInfoById(anyLong())).willReturn(response);

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
				.identifier("get-my-page-success")
				.tag(ApiTag.USER)
				.summary("마이페이지 API")
				.description("마이페이지에서 사용될 사용자의 정보를 불러옵니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}

	@ParameterizedApiTest
	@MethodSource("authenticationFailResults")
	@DisplayName("요청 시 인증에 실패하면 400에러를 반환한다.")
	void getMyPage_Fail_ByAuthentication(ErrorCode errorCode, String identifier) throws Exception {
		// given
		RuntimeException authenticationFailed = new AjajaException(errorCode);

		willThrow(authenticationFailed).given(getMyPageQuery).findUserInfoById(anyLong());

		// when
		var result = mockMvc.perform(get(USER_END_POINT)
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON));

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
				.identifier("get-my-page-fail-" + identifier)
				.tag(ApiTag.USER)
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
