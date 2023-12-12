package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.module.user.dto.UserRequest;

class ChangeReceiveControllerTest extends WebMvcTestSupport {

	@ApiTest
	void changeReceiveType_Success() throws Exception {
		// given
		UserRequest.Receive request = sut.giveMeOne(UserRequest.Receive.class);

		// when
		ResultActions result = mockMvc.perform(put(USER_END_POINT + "/receive")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isOk());
	}
}
