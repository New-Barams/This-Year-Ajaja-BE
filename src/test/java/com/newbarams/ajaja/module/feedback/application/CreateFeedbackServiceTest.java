package com.newbarams.ajaja.module.feedback.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.feedback.domain.FeedbackRepository;

class CreateFeedbackServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateFeedbackService createFeedbackService;

	@Mock
	private FeedbackRepository feedbackRepository;

	@Test
	@DisplayName("유저 id와 계획 id를 가지고 피드백 객체 정보를 생성한다.")
	void create_Success_WithNoException() {
		// given
		Long userId = 1L;
		Long planId = 1L;

		// when
		createFeedbackService.create(userId, planId);

		// then
		then(feedbackRepository).should(times(1)).save(any());
	}
}
