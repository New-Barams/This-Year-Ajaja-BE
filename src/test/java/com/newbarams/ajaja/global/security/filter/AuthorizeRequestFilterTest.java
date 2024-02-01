package com.newbarams.ajaja.global.security.filter;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
class AuthorizeRequestFilterTest {
	@Autowired
	private MockMvc mockMvc;

	@ParameterizedTest
	@ValueSource(strings = {"/posts", "/hello", "/tests", "/replies"})
	void filtering_Success_WithNotSupportUris(String uri) throws Exception {
		// given

		// when
		var result = mockMvc.perform(post(uri));

		// then
		result.andExpectAll(
			status().isNotFound(),
			jsonPath("$.httpStatus").value(NOT_FOUND.name()),
			jsonPath("$.errorName").value(NOT_SUPPORT_END_POINT.name()),
			jsonPath("$.errorMessage").value(NOT_SUPPORT_END_POINT.getMessage())
		);
	}
}
