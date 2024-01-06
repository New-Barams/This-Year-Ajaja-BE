package com.newbarams.ajaja.module.feedback.application;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.feedback.application.model.FeedbackPeriod;
import com.newbarams.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.dto.FeedbackResponse;
import com.newbarams.ajaja.module.feedback.infra.model.FeedbackInfo;
import com.newbarams.ajaja.module.feedback.mapper.FeedbackMapper;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;

class LoadFeedbackInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private LoadFeedbackInfoService loadFeedbackInfoService;

	@Mock
	private LoadPlanService loadPlanService;
	@Mock
	private FeedbackQueryRepository feedbackQueryRepository;
	@Mock
	private FeedbackMapper mapper;

	private List<FeedbackInfo> feedbacks;
	private PlanFeedbackInfo planFeedbackInfo;
	private FeedbackResponse.FeedbackInfo feedbackInfo;

	@BeforeEach
	void setUp() {
		feedbacks = sut.giveMeBuilder(FeedbackInfo.class)
			.set("feedbackMonth", 3)
			.set("feedbackDate", 15)
			.sampleList(4);

		List<FeedbackPeriod> periods = sut.giveMeBuilder(FeedbackPeriod.class)
			.set("remindMonth", 3)
			.set("remindDate", 15)
			.sampleList(4);

		planFeedbackInfo = sut.giveMeBuilder(PlanFeedbackInfo.class)
			.set("title", "계획")
			.set("createdYear", 2023)
			.set("remindMonth", 2)
			.set("remindDate", 15)
			.set("remindTime", 9)
			.set("periods", periods)
			.sample();
		feedbackInfo = sut.giveMeBuilder(FeedbackResponse.FeedbackInfo.class)
			.set("title", "계획")
			.sample();
	}

	@Test
	@DisplayName("계획에 해당하는 피드백 정보를 가져온다.")
	void loadFeedbackInfoByPlanId_Success_WithNoException() {
		// given
		Long userId = 1L;
		Long planId = 1L;

		given(loadPlanService.loadPlanFeedbackInfoByPlanId(anyLong(), anyLong())).willReturn(planFeedbackInfo);
		given(feedbackQueryRepository.findFeedbackInfosByPlanId(planId)).willReturn(feedbacks);
		given(mapper.toResponse(any(), anyList())).willReturn(feedbackInfo);

		// when
		FeedbackResponse.FeedbackInfo response = loadFeedbackInfoService.loadFeedbackInfoByPlanId(userId, planId);

		// then
		Assertions.assertThat(response.getTitle()).isEqualTo(planFeedbackInfo.title());
	}

	@Test
	@DisplayName("만일 계획 정보가 없다면 예외를 던진다.")
	void loadFeedbackInfoByPlanId_Fail_ByNotFoundPlan() {
		// given
		doThrow(AjajaException.class).when(loadPlanService).loadPlanFeedbackInfoByPlanId(anyLong(), anyLong());

		// when,then
		Assertions.assertThatException().isThrownBy(
			() -> loadFeedbackInfoService.loadFeedbackInfoByPlanId(anyLong(), anyLong())
		);
	}
}
