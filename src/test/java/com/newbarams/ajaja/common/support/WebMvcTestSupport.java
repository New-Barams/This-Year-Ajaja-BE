package com.newbarams.ajaja.common.support;

import static org.springframework.context.annotation.ComponentScan.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.newbarams.ajaja.module.remind.application.LoadRemindInfoService;
import com.newbarams.ajaja.module.remind.application.LoadSentRemindInfoService;
import com.newbarams.ajaja.module.user.application.ChangeReceiveTypeService;
import com.newbarams.ajaja.module.user.application.LoginService;
import com.newbarams.ajaja.module.user.application.LogoutService;
import com.newbarams.ajaja.module.user.application.ReissueTokenService;
import com.newbarams.ajaja.module.user.application.RenewNicknameService;
import com.newbarams.ajaja.module.user.application.SendVerificationEmailService;
import com.newbarams.ajaja.module.user.application.VerifyCertificationService;
import com.newbarams.ajaja.module.user.application.WithdrawService;
import com.newbarams.ajaja.module.user.domain.UserQueryRepository;

/**
 * Supports Cached Context On WebMvcTest with Monkey <br>
 * Scan Controllers By Annotation and Manage MockBeans <br>
 * @see ApiTest
 * @author hejow
 */
@WebMvcTest(
	includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class),
	excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MockController.class})
)
public abstract class WebMvcTestSupport extends MonkeySupport {
	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;

	// Auth
	@MockBean
	protected LoginService loginService;
	@MockBean
	protected ReissueTokenService reissueTokenService;

	// User
	@MockBean
	protected LogoutService logoutService;
	@MockBean
	protected WithdrawService withdrawService;
	@MockBean
	protected UserQueryRepository userQueryRepository;
	@MockBean
	protected RenewNicknameService renewNicknameService;
	@MockBean
	protected ChangeReceiveTypeService changeReceiveTypeService;
	@MockBean
	protected VerifyCertificationService verifyCertificationService;
	@MockBean
	protected SendVerificationEmailService sendVerificationEmailService;

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
	protected LoadRemindInfoService loadRemindInfoService;
	@MockBean
	protected LoadSentRemindInfoService loadSentRemindInfoService;

	// Ajaja
	@MockBean
	protected SwitchAjajaService switchAjajaService;
}
