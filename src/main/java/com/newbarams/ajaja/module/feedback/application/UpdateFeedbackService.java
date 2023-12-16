package com.newbarams.ajaja.module.feedback.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.plan.application.UpdatePlanAchieveService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateFeedbackService {
	private final FeedbackQueryRepository feedbackQueryRepository;
	private final UpdatePlanAchieveService updatePlanAchieveService;

	public void updateFeedback(Long feedbackId, int rate) {
		Feedback feedback = feedbackQueryRepository.findByFeedbackId(feedbackId)
			.orElseThrow(() -> AjajaException.withId(feedbackId, NOT_FOUND_FEEDBACK));

		if (feedback.isBeforeDeadline()) {
			feedbackQueryRepository.update(feedbackId, feedback.getAchieveName(rate)); // todo: 더티 체킹 적용 후 도메인 로직에 넣기
		}

		Long planId = feedback.getPlanId();
		updatePlanAchieve(planId);
	}

	private void updatePlanAchieve(Long planId) {
		int feedbackAverage = (int)feedbackQueryRepository.findAllFeedbackByPlanId(planId)
			.stream()
			.mapToInt(Feedback::getRate)
			.average()
			.orElse(0);

		updatePlanAchieveService.updatePlanAchieve(planId, feedbackAverage);
	}
}
