package me.ajaja.module.feedback.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.feedback.domain.Achieve;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.domain.FeedbackRepository;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.RemindDate;
import me.ajaja.module.remind.application.port.out.FindTargetRemindQuery;

class UpdateFeedbackServiceTest extends MockTestSupport {
	@InjectMocks
	private UpdateFeedbackService updateFeedbackService;

	@Mock
	private FindTargetRemindQuery findTargetRemindQuery;
	@Mock
	private FeedbackQueryRepository feedbackQueryRepository;
	@Mock
	private FeedbackRepository feedbackRepository;
	@Mock
	private Plan mockPlan;

	@Nested
	class DeadlineTest {
		private RemindDate remindDate = sut.giveMeBuilder(RemindDate.class)
			.set("remindMonth", 2)
			.set("remindDay", 17)
			.sample();

		@Test
		@DisplayName("기간 내에 피드백을 시행할 경우 성공한다.")
		void updateFeedback_Success_WithNoException() {
			// given
			given(findTargetRemindQuery.loadByUserIdAndPlanId(anyLong(), anyLong())).willReturn(mockPlan);
			given(mockPlan.getFeedbackPeriod(any())).willReturn(TimeValue.now());
			given(feedbackQueryRepository.existByPlanIdAndPeriod(any(), any())).willReturn(false);
			doNothing().when(feedbackRepository).save(any());

			// when,then
			assertThatNoException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 1L, 50, "fighting"));
		}

		@Test
		@DisplayName("타인의 계획을 피드백 하려는 경우에는 예외를 던진다.")
		void updateFeedback_Fail_ByNotFoundPlan() {
			// given
			doThrow(AjajaException.class).when(findTargetRemindQuery).loadByUserIdAndPlanId(anyLong(), anyLong());

			// when,then
			assertThatException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 1L, 50, "fighting")
			);
		}

		@Test
		@DisplayName("피드백 기간이 아닐 경우에는 예외를 던진다.")
		void updateFeedback_Fail_ByExpiredFeedbackPeriod() {
			// given
			given(findTargetRemindQuery.loadByUserIdAndPlanId(anyLong(), anyLong())).willReturn(mockPlan);
			doThrow(AjajaException.class).when(mockPlan).getFeedbackPeriod(any());

			// when,then
			assertThatException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 1L, 50, "fighting")
			);
		}

		@Test
		@DisplayName("해당 기간에 이미 피드백을 한 경우에는 예외를 던진다.")
		void updateFeedback_Fail_ByAlreadyFeedbackFound() {
			// given
			given(findTargetRemindQuery.loadByUserIdAndPlanId(anyLong(), anyLong())).willReturn(mockPlan);
			given(mockPlan.getFeedbackPeriod(any())).willReturn(TimeValue.now());
			given(feedbackQueryRepository.existByPlanIdAndPeriod(any(), any())).willReturn(true);

			// when,then
			assertThatException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 1L, 50, "fighting")
			);
		}
	}

	@Nested
	class AchieveTest {
		@Test
		@DisplayName("선택지 외 다른 항목이 오면 예외를 던진다.")
		void findRate_Fail_ByException() {
			// given
			int rate = 10;

			// when,then
			assertThatThrownBy(() -> Achieve.of(rate))
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("올바른 선택지를 입력하면 피드백 점수를 받는다")
		void findRate_Success_WithNoException() {
			// given
			int rate = 50;

			// when
			Achieve achieve = Achieve.of(rate);

			// when,then
			assertThat(achieve).isEqualTo(Achieve.SOSO);
		}
	}
}
