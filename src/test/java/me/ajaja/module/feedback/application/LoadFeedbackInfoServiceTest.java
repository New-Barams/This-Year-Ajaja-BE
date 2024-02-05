package me.ajaja.module.feedback.application;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.feedback.application.model.FeedbackPeriod;
import me.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.dto.FeedbackResponse;
import me.ajaja.module.feedback.infra.model.FeedbackInfo;
import me.ajaja.module.feedback.mapper.FeedbackMapper;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.remind.application.port.out.FindTargetRemindQuery;

class LoadFeedbackInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private LoadFeedbackInfoService loadFeedbackInfoService;

	@Mock
	private FindTargetRemindQuery findTargetRemindQuery;
	@Mock
	private FeedbackQueryRepository feedbackQueryRepository;
	@Mock
	private FeedbackMapper mapper;
	@Mock
	private PlanMapper planMapper;

	private List<FeedbackInfo> feedbacks;
	private PlanFeedbackInfo planFeedbackInfo;
	private FeedbackResponse.FeedbackInfo feedbackInfo;
	private FeedbackResponse.RemindFeedback remindFeedback;

	@BeforeEach
	void setUp() {
		feedbacks = sut.giveMeBuilder(FeedbackInfo.class)
			.set("feedbackMonth", 3)
			.set("feedbackDate", 16)
			.set("feedbacked", true)
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

		remindFeedback = sut.giveMeBuilder(FeedbackResponse.RemindFeedback.class)
			.sample();
	}

	@Test
	@DisplayName("계획에 해당하는 피드백 정보를 가져온다.")
	void loadFeedbackInfoByPlanId_Success_WithNoException() {
		// given
		Long userId = 1L;
		Long planId = 1L;

		given(planMapper.toModel(findTargetRemindQuery.loadByUserIdAndPlanId(anyLong(), anyLong()))).willReturn(
			planFeedbackInfo);
		given(feedbackQueryRepository.findFeedbackInfosByPlanId(planId)).willReturn(feedbacks);
		given(mapper.toResponse(any(), any(), any())).willReturn(remindFeedback);
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
		doThrow(AjajaException.class).when(findTargetRemindQuery).loadByUserIdAndPlanId(anyLong(), anyLong());

		// when,then
		Assertions.assertThatException().isThrownBy(
			() -> loadFeedbackInfoService.loadFeedbackInfoByPlanId(anyLong(), anyLong())
		);
	}
}
