package com.newbarams.ajaja.module.plan.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.common.support.WebMvcTestSupport;

class PlanControllerTest extends WebMvcTestSupport {

	@ApiTest
	void getPlanAchieve_Success_WithNoException() throws Exception {
		// given
		given(getPlanAchieveService.calculatePlanAchieve(anyLong())).willReturn(50);

		// when, then
		mockMvc.perform(get(PLAN_END_POINT + "/1/feedbacks")
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
