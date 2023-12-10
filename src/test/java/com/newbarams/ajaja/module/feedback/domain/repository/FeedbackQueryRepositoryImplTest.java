package com.newbarams.ajaja.module.feedback.domain.repository;

import java.time.Instant;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackRepository;
import com.newbarams.ajaja.module.feedback.infra.FeedbackQueryRepositoryImpl;

@SpringBootTest
@Transactional
class FeedbackQueryRepositoryImplTest extends MockTestSupport {
	@Autowired
	private FeedbackQueryRepositoryImpl feedbackRepositoryCustom;
	@Autowired
	private FeedbackRepository feedbackRepository;
	private Feedback feedback;

	@BeforeEach
	void setUp() {
		feedbackRepository.save(monkey.giveMeBuilder(Feedback.class)
			.set("isDeleted", false)
			.set("planId", 1L)
			.set("createdAt", Instant.now())
			.sample());
	}

	@Test
	@DisplayName("플랜 id에 맞는 피드백 정보를 가져온다.")
	void findRemindInfoByPlanId_Success_WithNoException() {
		// given
		Long planId = 1L;

		// when
		List<Feedback> feedbacks = feedbackRepositoryCustom.findAllFeedbackByPlanId(planId);

		// then
		Assertions.assertThat(feedbacks.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("만약 플랜id에 맞는 피드백 정보가 없으면 빈 리스트를 반환한다.")
	void findNoRemindInfoByPlanId_Success_WithNoException() {
		// given
		Long planId = 2L;

		// when
		List<Feedback> feedbacks = feedbackRepositoryCustom.findAllFeedbackByPlanId(planId);

		// then
		Assertions.assertThat(feedbacks.size()).isZero();
	}
}
