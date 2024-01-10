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
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.dto.UserRequest;

class ChangeReceiveControllerTest extends WebMvcTestSupport {

	@ApiTest
	void changeReceiveType_Success() throws Exception {
		// given
		UserRequest.Receive request = new UserRequest.Receive(User.ReceiveType.BOTH);

		willDoNothing().given(changeReceiveTypeUseCase).change(anyLong(), any());

		// when
		var result = mockMvc.perform(put(USER_END_POINT + "/receive")
			.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpectAll(
			status().isOk()
		);

		// docs
		result.andDo(
			RestDocument.builder()
				.identifier("update-receive-type")
				.tag(ApiTag.USER)
				.summary("수신 방법 변경 API")
				.description("리마인드를 수신할 방법을 변경합니다.")
				.secured(true)
				.result(result)
				.generateDocs()
		);
	}
}
