package com.newbarams.ajaja.common.support;

import static org.apache.commons.codec.CharEncoding.*;
import static org.springframework.context.annotation.ComponentScan.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbarams.ajaja.common.annotation.ApiTest;
import com.newbarams.ajaja.global.mock.MockController;
import com.newbarams.ajaja.global.security.jwt.util.JwtParser;
import com.newbarams.ajaja.module.ajaja.application.SwitchAjajaService;
import com.newbarams.ajaja.module.auth.application.port.in.LoginUseCase;
import com.newbarams.ajaja.module.auth.application.port.in.ReissueTokenUseCase;
import com.newbarams.ajaja.module.feedback.application.LoadFeedbackInfoService;
import com.newbarams.ajaja.module.feedback.application.LoadTotalAchieveService;
import com.newbarams.ajaja.module.feedback.application.UpdateFeedbackService;
import com.newbarams.ajaja.module.plan.application.CreatePlanService;
import com.newbarams.ajaja.module.plan.application.DeletePlanService;
import com.newbarams.ajaja.module.plan.application.LoadPlanInfoService;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.application.UpdatePlanService;
import com.newbarams.ajaja.module.plan.application.UpdateRemindInfoService;
import com.newbarams.ajaja.module.remind.application.LoadRemindInfoService;
import com.newbarams.ajaja.module.user.application.port.in.ChangeReceiveTypeUseCase;
import com.newbarams.ajaja.module.user.application.port.in.LogoutUseCase;
import com.newbarams.ajaja.module.user.application.port.in.RenewNicknameUseCase;
import com.newbarams.ajaja.module.user.application.port.in.SendVerificationEmailUseCase;
import com.newbarams.ajaja.module.user.application.port.in.VerifyCertificationUseCase;
import com.newbarams.ajaja.module.user.application.port.in.WithdrawUseCase;
import com.newbarams.ajaja.module.user.application.port.out.GetMyPagePort;

/**
 * Supports Cached Context On WebMvcTest with Monkey <br>
 * When Authentication is required USE @ApiTest
 * @see ApiTest
 * @author hejow
 */
@WebMvcTest(excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MockController.class))
@ExtendWith(RestDocumentationExtension.class)
public abstract class WebMvcTestSupport extends MonkeySupport {
	private static final String ANY_END_POINT = "/**";

	protected static final String USER_END_POINT = "/users";
	protected static final String PLAN_END_POINT = "/plans";
	protected static final String FEEDBACK_END_POINT = "/feedbacks";
	protected static final String BEARER_TOKEN = "Bearer eyJhbGxMiJ9.eyJzWpvdyJ9.avFKonhbIIhEg8H1dycQkhQ";

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;

	@BeforeEach
	void setup(
		WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation
	) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.alwaysDo(print())
			.apply(springSecurity())
			.apply(documentationConfiguration(restDocumentation))
			.addFilter(new CharacterEncodingFilter(UTF_8, true))
			.defaultRequest(post(ANY_END_POINT).with(csrf().asHeader()))
			.defaultRequest(get(ANY_END_POINT).with(csrf().asHeader()))
			.defaultRequest(put(ANY_END_POINT).with(csrf().asHeader()))
			.defaultRequest(delete(ANY_END_POINT).with(csrf().asHeader()))
			.build();
	}

	/**
	 * Caching Mock Beans
	 */
	@MockBean
	protected JwtParser jwtParser; // todo: delete after authentication aop applied

	// Auth
	@MockBean
	protected LoginUseCase loginUseCase;
	@MockBean
	protected ReissueTokenUseCase reissueTokenUseCase;

	// User
	@MockBean
	protected ChangeReceiveTypeUseCase changeReceiveTypeUseCase;
	@MockBean
	protected LogoutUseCase logoutUseCase;
	@MockBean
	protected RenewNicknameUseCase renewNicknameUseCase;
	@MockBean
	protected SendVerificationEmailUseCase sendVerificationEmailUseCase;
	@MockBean
	protected VerifyCertificationUseCase verifyCertificationUseCase;
	@MockBean
	protected WithdrawUseCase withdrawUseCase;
	@MockBean
	protected GetMyPagePort getMyPagePort;

	// Plan
	@MockBean
	protected CreatePlanService createPlanService;
	@MockBean
	protected LoadPlanService getPlanService;
	@MockBean
	protected DeletePlanService deletePlanService;
	@MockBean
	protected UpdatePlanService updatePlanService;
	@MockBean
	protected LoadPlanInfoService loadPlanInfoService;
	@MockBean
	protected UpdateRemindInfoService updateRemindInfoService;
	@MockBean
	protected SwitchAjajaService switchAjajaService;

	// Feedback
	@MockBean
	protected UpdateFeedbackService updateFeedbackService;
	@MockBean
	protected LoadTotalAchieveService loadTotalAchieveService;
	@MockBean
	protected LoadFeedbackInfoService loadFeedbackInfoService;

	// Remind
	@MockBean
	protected LoadRemindInfoService loadRemindInfoService;
}
