package com.newbarams.ajaja.module.feedback.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.module.feedback.dto.UpdateFeedback;

class FeedbackControllerTest extends WebMvcTestSupport {

	@ApiTest
	void updateFeedback_Success_WithNoException() throws Exception {
		// given
		UpdateFeedback updateFeedback = new UpdateFeedback(50, "fighting");

		doNothing().when(updateFeedbackService).updateFeedback(anyLong(), anyInt(), anyString());

		// when,then
		mockMvc.perform(MockMvcRequestBuilders.post(FEEDBACK_END_POINT + "/1")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateFeedback)))
			.andExpect(status().isOk());
	}
}
