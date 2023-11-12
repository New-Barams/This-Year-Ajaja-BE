package com.newbarams.ajaja.module.plan.presentation;

import static org.mockito.ArgumentMatchers.*;
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

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.plan.application.CreatePlanService;
import com.newbarams.ajaja.module.plan.application.DeletePlanService;
import com.newbarams.ajaja.module.plan.application.GetPlanAchieveService;
import com.newbarams.ajaja.module.plan.application.GetPlanInfoService;
import com.newbarams.ajaja.module.plan.application.GetPlanService;
import com.newbarams.ajaja.module.plan.application.UpdatePlanService;

@AutoConfigureMockMvc
@WebMvcTest(controllers = PlanController.class)
class PlanControllerTest extends MockTestSupport {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GetPlanAchieveService getPlanAchieveService;
	@MockBean
	private GetPlanService getPlanService;
	@MockBean
	private CreatePlanService createPlanService;
	@MockBean
	private DeletePlanService deletePlanService;
	@MockBean
	private UpdatePlanService updatePlanService;
	@MockBean
	private GetPlanInfoService getPlanInfoService;

	@Test
	@WithMockUser
	void getPlanAchieve() throws Exception {
		given(getPlanAchieveService.calculatePlanAchieve(anyLong())).willReturn(50);

		mockMvc.perform(MockMvcRequestBuilders.get("/plans/1/feedbacks")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	void getPlanInfo() throws Exception {
		given(getPlanInfoService.loadPlanInfo(any())).willReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get("/plans/main/1")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
