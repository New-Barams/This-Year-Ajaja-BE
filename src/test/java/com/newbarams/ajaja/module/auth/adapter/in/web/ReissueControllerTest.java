package com.newbarams.ajaja.module.auth.adapter.in.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.module.auth.dto.AuthRequest;

class ReissueControllerTest extends WebMvcTestSupport {

	@ApiTest
	void reissue_Success() throws Exception {
		// given
		AuthRequest.Reissue request = sut.giveMeOne(AuthRequest.Reissue.class);

		// when
		ResultActions result = mockMvc.perform(post("/reissue")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().isOk());
	}
}
