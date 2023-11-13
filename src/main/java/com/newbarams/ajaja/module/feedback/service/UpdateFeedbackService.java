package com.newbarams.ajaja.module.feedback.service;

import static com.newbarams.ajaja.global.common.error.AjajaErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.exeption.AjajaException;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;
import com.newbarams.ajaja.module.plan.application.UpdatePlanAchieveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateFeedbackService {
	private final FeedbackRepository feedbackRepository;
	private final UpdatePlanAchieveService updatePlanAchieveService;

	@Transactional
	public void updateFeedback(Long feedbackId, int rate) {
		Feedback feedback = feedbackRepository.findById(feedbackId)
			.orElseThrow(() -> AjajaException.withId(feedbackId, NOT_FOUND_FEEDBACK));

		feedback.checkDeadline();
		feedback.updateAchieve(rate);

		Long planId = feedback.getPlanId();

		updatePlanAchieve(planId);
	}

	private void updatePlanAchieve(Long planId) {
		int feedbackAverage = (int)feedbackRepository.findAllByPlanIdIdAndCreatedYear(planId)
			.stream()
			.mapToInt(Feedback::getRate)
			.average()
			.orElse(0);

		updatePlanAchieveService.updatePlanAchieve(planId, feedbackAverage);
	}
}
