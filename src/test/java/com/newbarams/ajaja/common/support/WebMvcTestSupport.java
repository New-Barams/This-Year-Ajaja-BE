package com.newbarams.ajaja.common.support;

import static org.springframework.context.annotation.ComponentScan.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.global.mock.MockController;
import com.newbarams.ajaja.module.ajaja.application.SwitchAjajaService;
import com.newbarams.ajaja.module.feedback.application.GetTotalAchieveService;
import com.newbarams.ajaja.module.feedback.application.UpdateFeedbackService;
import com.newbarams.ajaja.module.plan.application.CreatePlanService;
import com.newbarams.ajaja.module.plan.application.DeletePlanService;
import com.newbarams.ajaja.module.plan.application.GetPlanAchieveService;
import com.newbarams.ajaja.module.plan.application.LoadPlanInfoService;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.application.UpdatePlanService;
import com.newbarams.ajaja.module.remind.application.LoadSentRemindInfoService;
import com.newbarams.ajaja.module.user.application.service.UserMockBeans;
import com.newbarams.ajaja.module.user.domain.UserQueryRepository;

/**
 * Supports Cached Context On WebMvcTest with Monkey <br>
 * Scan All Controllers By Annotation and Manage MockBeans <br>
 * When Authentication is required USE @ApiTest
 * @see ApiTest
 * @author hejow
 */
@WebMvcTest(
	includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class),
	excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MockController.class)
)
@Import(UserMockBeans.class)
public abstract class WebMvcTestSupport extends MonkeySupport {
	protected static final String USER_END_POINT = "/users";
	protected static final String PLAN_END_POINT = "/plans";
	protected static final String FEEDBACK_END_POINT = "/feedbacks";

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;

	// User
	@MockBean
	protected UserQueryRepository userQueryRepository;

	// Plan
	@MockBean
	protected CreatePlanService createPlanService;
	@MockBean
	protected LoadPlanService getPlanService;
	@MockBean
	protected DeletePlanService deletePlanService;
	@MockBean
	protected GetPlanAchieveService getPlanAchieveService;
	@MockBean
	protected UpdatePlanService updatePlanService;
	@MockBean
	protected LoadPlanInfoService loadPlanInfoService;

	// Feedback
	@MockBean
	protected UpdateFeedbackService updateFeedbackService;
	@MockBean
	protected GetTotalAchieveService getTotalAchieveService;

	// Remind
	@MockBean
	protected LoadSentRemindInfoService loadSentRemindInfoService;

	// Ajaja
	@MockBean
	protected SwitchAjajaService switchAjajaService;
}
