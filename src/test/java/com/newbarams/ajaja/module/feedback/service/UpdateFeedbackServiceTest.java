package com.newbarams.ajaja.module.feedback.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

@ExtendWith(MockitoExtension.class)
class UpdateFeedbackServiceTest {
	@InjectMocks
	private UpdateFeedbackService updateFeedbackService;

	@Mock
	private FeedbackRepository feedbackRepository;

	@Mock
	private Feedback mockFeedback;

	@Nested
	class DeadlineTest {

		@Test
		@DisplayName("기간 내에 피드백을 시행할 경우 성공한다.")
		void updateTest_Success_WithNoException() {
			// given
			Instant remindTime = Instant.now().minus(15, ChronoUnit.DAYS);

			given(mockFeedback.getCreatedAt()).willReturn(remindTime);
			given(feedbackRepository.findById(any())).willReturn(Optional.of(mockFeedback));

			// when,then
			assertThatNoException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 50));
		}

		@Test
		@DisplayName("데드라인이 지난 피드백을 할 경우 예외를 던진다.")
		void updateTest_Fail_ByIllegalAccessException() {
			// given
			Instant remindTime = Instant.now().minus(50, ChronoUnit.DAYS);

			given(mockFeedback.getCreatedAt()).willReturn(remindTime);
			given(feedbackRepository.findById(any())).willReturn(Optional.of(mockFeedback));

			// when,then
			assertThatException().isThrownBy(
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
				() -> Achieve.findAchieve(rate));
		}

		@Test
		@DisplayName("올바른 선택지를 입력하면 피드백 점수를 받는다")
		void findRate_Success_WithNoException() {
			// given
			int rate = 50;

			// when
			Achieve achieve = Achieve.findAchieve(rate);

			// when,then
			assertThat(achieve).isEqualTo(Achieve.SOSO);
		}
	}
}
