package com.newbarams.ajaja.module.plan.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.newbarams.ajaja.common.support.WebMvcTestSupport;

class PlanControllerTest extends WebMvcTestSupport {

	@Test
	@WithMockUser
	void getPlanAchieve_Success_WithNoException() throws Exception {
		given(getPlanAchieveService.calculatePlanAchieve(anyLong())).willReturn(50);

		mockMvc.perform(get("/plans/1/feedbacks")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	// @Test
	// @WithMockUser
	// void getPlanInfo_Success_WithNoException() throws Exception {
	// 	given(loadPlanInfoService.loadPlanInfo(any())).willReturn(null);
	//
	// 	mockMvc.perform(MockMvcRequestBuilders.get("/plans/main")
	// 			.with(csrf())
	// 			.contentType(MediaType.APPLICATION_JSON))
	// 		.andExpect(status().isOk());
	// }
}
