package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;

class GetMyPageControllerTest extends WebMvcTestSupport {

	@ApiTest
	void getMyPage_Success() throws Exception {
		// given

		// when
		ResultActions result = mockMvc.perform(get(USER_END_POINT)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
	}
}
