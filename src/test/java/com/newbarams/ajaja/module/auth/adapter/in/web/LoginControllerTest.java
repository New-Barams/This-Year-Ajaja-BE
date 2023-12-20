package com.newbarams.ajaja.module.auth.adapter.in.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.module.auth.dto.AuthRequest;

class LoginControllerTest extends WebMvcTestSupport {

	@Test
	void login_Success() throws Exception {
		// given
		AuthRequest.Login request = sut.giveMeOne(AuthRequest.Login.class);

		// when
		ResultActions result = mockMvc.perform(post("/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)));

		// then
		result.andExpect(status().is3xxRedirection()); // oauth redirect
	}
}
