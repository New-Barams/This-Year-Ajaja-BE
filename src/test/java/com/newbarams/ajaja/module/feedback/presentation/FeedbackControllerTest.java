package com.newbarams.ajaja.module.feedback.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.newbarams.ajaja.common.support.WebMvcTestSupport;
import com.newbarams.ajaja.module.feedback.dto.UpdateFeedback;

class FeedbackControllerTest extends WebMvcTestSupport {

	@Test
	@WithMockUser
	void updateFeedback_Success_WithNoException() throws Exception {
		// given
		UpdateFeedback updateFeedback = new UpdateFeedback(50);

		doNothing().when(updateFeedbackService).updateFeedback(anyLong(), anyInt());

		// when,then
		mockMvc.perform(MockMvcRequestBuilders.post("/feedbacks/1")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateFeedback)))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	void getTotalAchieve_Success_WithNoException() throws Exception {
		given(getTotalAchieveService.calculateTotalAchieve(anyLong())).willReturn(50);

		mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
