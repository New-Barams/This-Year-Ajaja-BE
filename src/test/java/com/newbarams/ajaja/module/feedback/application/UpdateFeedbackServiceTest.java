package com.newbarams.ajaja.module.feedback.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.feedback.domain.Achieve;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.domain.FeedbackRepository;

class UpdateFeedbackServiceTest extends MockTestSupport {
	@InjectMocks
	private UpdateFeedbackService updateFeedbackService;

	@Mock
	private FeedbackQueryRepository feedbackQueryRepository;
	@Mock
	private FeedbackRepository feedbackRepository;
	@Mock
	private Feedback mockFeedback;

	@Nested
	class DeadlineTest {
		@Test
		@DisplayName("기간 내에 피드백을 시행할 경우 성공한다.")
		void updateFeedback_Success_WithNoException() {
			// given
			given(feedbackQueryRepository.findByFeedbackId(any())).willReturn(Optional.of(mockFeedback));
			doNothing().when(feedbackRepository).save(any());

			// when,then
			assertThatNoException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 50, "fighting"));
		}

		@Test
		@DisplayName("데드라인이 지난 피드백을 할 경우 예외를 던진다.")
		void updateFeedback_Fail_ByIllegalAccessException() {
			// given
			given(feedbackQueryRepository.findByFeedbackId(any())).willReturn(Optional.of(mockFeedback));
			doThrow(AjajaException.class).when(mockFeedback).updateFeedback(50, "fighting");

			// when,then
			assertThatException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 50, "fighting")
			);
		}

		@Test
		@DisplayName("데드라인이 지난 피드백을 할 경우 예외를 던진다.")
		void updateFeedback_Fail_ByNotFoundFeedback() {
			// given
			given(feedbackQueryRepository.findByFeedbackId(any())).willReturn(Optional.empty());

			// when,then
			assertThatException().isThrownBy(
				() -> updateFeedbackService.updateFeedback(1L, 50, "fighting")
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
