package com.newbarams.ajaja.module.feedback.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.newbarams.ajaja.module.feedback.domain.Achieve;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;
import com.newbarams.ajaja.module.plan.application.UpdatePlanAchieveService;
import com.newbarams.ajaja.module.plan.domain.Plan;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@ExtendWith(MockitoExtension.class)
class UpdateFeedbackServiceTest {
	private final FixtureMonkey sut = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@InjectMocks
	private UpdateFeedbackService updateFeedbackService;

	@Mock
	private FeedbackRepository feedbackRepository;

	@Mock
	private UpdatePlanAchieveService updatePlanAchieveService;

	@Mock
	private Feedback mockFeedback;

	@Nested
	class DeadlineTest {

		@Test
		@DisplayName("기간 내에 피드백을 시행할 경우 성공한다.")
		void updateTest_Success_WithNoException() {
			// given
			List<Feedback> feedbacks = sut.giveMe(Feedback.class, 2);

			Plan plan = sut.giveMeOne(Plan.class);

			// mock
			given(feedbackRepository.findById(any())).willReturn(Optional.of(mockFeedback));
			doNothing().when(updatePlanAchieveService).updatePlanAchieve(anyLong(), anyInt());
			given(feedbackRepository.findAllFeedbackByPlanId(any())).willReturn(feedbacks);

			// when,then
			assertThatNoException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 50));

			assertThat(plan.getAchieveRate()).isNotZero();
		}

		@Test
		@DisplayName("데드라인이 지난 피드백을 할 경우 예외를 던진다.")
		void updateTest_Fail_ByIllegalAccessException() {
			// given
			given(feedbackRepository.findById(any())).willReturn(Optional.of(mockFeedback));

			// when,then
			assertThatNoException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 50));
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
			assertThatThrownBy(
				() -> Achieve.of(rate));
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
