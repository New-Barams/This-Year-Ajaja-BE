package com.newbarams.ajaja.module.feedback.application;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.dto.FeedbackResponse;
import com.newbarams.ajaja.module.feedback.infra.model.FeedbackInfo;
import com.newbarams.ajaja.module.feedback.mapper.FeedbackMapper;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;

class LoadFeedbackInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private LoadFeedbackInfoService loadFeedbackInfoService;

	@Mock
	private LoadPlanService loadPlanService;
	@Mock
	private FeedbackQueryRepository feedbackQueryRepository;
	@Mock
	private FeedbackMapper mapper;

	@Test
	void findAllFeedback_Success_WithNoException() {
		// given
		Long planId = 1L;
		Plan plan = sut.giveMeBuilder(Plan.class)
			.set("isDeleted", false)
			.sample();

		List<FeedbackInfo> feedbacks = sut.giveMe(FeedbackInfo.class, 4);
		List<FeedbackResponse.RemindedFeedback> reminds = sut.giveMe(FeedbackResponse.RemindedFeedback.class, 4);

		FeedbackResponse.FeedbackInfo response = sut.giveMeBuilder(FeedbackResponse.FeedbackInfo.class)
			.set("feedbacks", reminds).sample();

		given(loadPlanService.loadPlanOrElseThrow(anyLong())).willReturn(plan);
		given(feedbackQueryRepository.findFeedbackInfosByPlanId(planId)).willReturn(feedbacks);
		given(mapper.toResponse(plan, feedbacks)).willReturn(response);

		// when
		FeedbackResponse.FeedbackInfo feedbackInfo = loadFeedbackInfoService.loadFeedbackInfoByPlanId(planId);

		// then
		Assertions.assertThat(feedbackInfo.getFeedbacks().size()).isEqualTo(4);
	}

	@Test
	void findAllFeedback_Fail_ByNotFoundPlan() {
		// given
		doThrow(AjajaException.class).when(loadPlanService).loadPlanOrElseThrow(anyLong());

		// when,then
		Assertions.assertThatException().isThrownBy(
			() -> loadFeedbackInfoService.loadFeedbackInfoByPlanId(anyLong())
		);
	}
}
