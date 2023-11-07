package com.newbarams.ajaja.module.feedback.service;

import static org.assertj.core.api.Assertions.*;

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

	private Feedback feedback;

	@Nested
	class DeadlineTest {

		// @Test
		// @DisplayName("기간 내에 피드백을 시행할 경우 성공한다.")
		// void updateTest_Success_WithNoException() {
		// 	// given
		// 	feedback = new Feedback(1L, 1L, Achieve.BAD);
		//
		// 	given(feedbackRepository.findById(any())).willReturn(Optional.of(feedback));
		//
		// 	// when,then
		// 	assertThatNoException().isThrownBy(
		// 		() -> updateFeedbackService.updateFeedback(1L, 50));
		// }
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

	@Nested
	class SaveFeedbackTest {
		// @Test
		// @DisplayName("피드백을 반영한 엔티티를 저장한다.")
		// void saveFeedback_Success_WithNoException() throws IllegalAccessException {
		// 	// given
		// 	String remind = "2024-02-20 10:20:30.0";
		// 	Timestamp remindTime = Timestamp.valueOf(remind);
		//
		// 	String deadLineDate = "2024-03-20 10:20:30.0";
		// 	Timestamp deadline = Timestamp.valueOf(deadLineDate);
		//
		// 	String now = "2024-02-27 10:20:30.0";
		// 	Timestamp nowTime = Timestamp.valueOf(now);
		//
		// 	feedback = new Feedback(1L, 1L, Achieve.BAD);
		//
		// 	given(feedbackRepository.save(any())).willReturn(1L);
		// 	given(feedbackRepository.findById(any())).willReturn(Optional.of(feedback));
		//
		// 	// when
		// 	updateFeedbackService.updateFeedback(1L, 50);
		//
		// 	// then
		// 	assertThat(feedbackRepository.findById(1L)).usingRecursiveComparison().isEqualTo(feedback);
		// }
	}
}
