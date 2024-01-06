package com.newbarams.ajaja.module.feedback.infra;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.domain.Achieve;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackRepository;
import com.newbarams.ajaja.module.feedback.infra.model.FeedbackInfo;

@SpringBootTest
@Transactional
class FeedbackQueryRepositoryImplTest {
	@Autowired
	private FeedbackQueryRepositoryImpl feedbackQueryRepository;
	@Autowired
	private FeedbackRepository feedbackRepository;

	private Feedback feedback;

	@BeforeEach
	void setUp() {
		feedback = new Feedback(1L, 1L, 1L, Achieve.GOOD, "아좌좌", new TimeValue(), new TimeValue());
		feedbackRepository.save(feedback);
	}

	@Test
	@DisplayName("플랜 id에 맞는 피드백 정보를 가져온다.")
	void findRemindInfoByPlanId_Success_WithNoException() {
		// given
		Long planId = 1L;

		// when
		List<Feedback> feedbacks = feedbackQueryRepository.findAllFeedbackByPlanId(planId);

		// then
		assertThat(feedbacks).hasSize(1);
	}

	@Test
	@DisplayName("만약 플랜id에 맞는 피드백 정보가 없으면 빈 리스트를 반환한다.")
	void findNoRemindInfoByPlanId_Success_WithNoException() {
		// given
		Long planId = 2L;

		// when
		List<Feedback> feedbacks = feedbackQueryRepository.findAllFeedbackByPlanId(planId);

		// then
		assertThat(feedbacks).isEmpty();
	}

	@Test
	void findByPlanIdAndPeriod_Success_WithNoException() {
		// given
		Long planId = 2L;

		// when
		boolean isFeedbacked = feedbackQueryRepository.existByPlanIdAndPeriod(planId, new TimeValue());

		// then
		Assertions.assertThat(isFeedbacked).isFalse();
	}

	@Test
	@DisplayName("계획에 해당하는 피드백 정보들을 가져온다.")
	void findAllFeedbackInfosByPlanId_Success_WithNoException() {
		// given
		Long planId = 1L;

		// when
		List<FeedbackInfo> feedbackInfos = feedbackQueryRepository.findFeedbackInfosByPlanId(planId);

		// then
		assertThat(feedbackInfos.size()).isEqualTo(1);
	}
}
