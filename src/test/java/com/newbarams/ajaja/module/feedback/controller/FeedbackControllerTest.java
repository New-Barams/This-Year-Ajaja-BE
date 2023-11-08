package com.newbarams.ajaja.module.feedback.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbarams.ajaja.module.feedback.domain.dto.UpdateFeedback;
import com.newbarams.ajaja.module.feedback.service.GetTotalAchieveService;
import com.newbarams.ajaja.module.feedback.service.UpdateFeedbackService;

@AutoConfigureMockMvc
@WebMvcTest(controllers = FeedbackController.class)
class FeedbackControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UpdateFeedbackService updateFeedbackService;

	@MockBean
	private GetTotalAchieveService getTotalAchieveService;

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
		given(getTotalAchieveService.loadTotalAchieve(anyLong())).willReturn(50);

		mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/1")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
