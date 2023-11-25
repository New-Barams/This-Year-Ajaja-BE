package com.newbarams.ajaja.module.feedback.domain.repository;

import java.time.Instant;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.feedback.domain.Feedback;

@SpringBootTest
@Transactional
class FeedbackRepositoryCustomImplTest extends MockTestSupport {
	@Autowired
	private FeedbackRepositoryCustomImpl feedbackRepositoryCustom;
	@Autowired
	private FeedbackRepository feedbackRepository;
	private Feedback feedback;

	@BeforeEach
	void setUp() {
		feedback = feedbackRepository.save(monkey.giveMeBuilder(Feedback.class)
			.set("isDeleted", false)
			.set("planId", 1L)
			.set("createdAt", Instant.now())
			.sample());
	}

	@Test
	void findRemindInfoByPlanId_Success_WithNoException() {
		// given
		Long planId = 1L;

		// when
		List<Feedback> feedbacks = feedbackRepositoryCustom.findAllByPlanIdIdAndCreatedYear(planId);

		// then
		Assertions.assertThat(feedbacks.size()).isEqualTo(1);
	}

	@Test
	void findNoRemindInfoByPlanId_Success_WithNoException() {
		// given
		Long planId = 2L;

		// when
		List<Feedback> feedbacks = feedbackRepositoryCustom.findAllByPlanIdIdAndCreatedYear(planId);

		// then
		Assertions.assertThat(feedbacks.size()).isZero();
	}
}
