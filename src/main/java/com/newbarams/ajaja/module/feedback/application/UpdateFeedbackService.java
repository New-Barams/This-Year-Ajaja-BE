package com.newbarams.ajaja.module.feedback.application;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.exception.ErrorCode;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.domain.FeedbackRepository;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindDate;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateFeedbackService {
	private final LoadPlanService loadPlanService;
	private final FeedbackQueryRepository feedbackQueryRepository;
	private final FeedbackRepository feedbackRepository;

	public void updateFeedback(Long userId, Long planId, int rate, String message) {
		Plan plan = loadPlanService.loadByUserIdAndPlanId(userId, planId);
		TimeValue current = new TimeValue();

		RemindDate feedbackPeriod = plan.getFeedbackPeriod(current);
		Instant periodInstant = TimeValue.parseInstant(current.getYear(), feedbackPeriod.getRemindMonth(),
			feedbackPeriod.getRemindDay(), plan.getRemindTime());

		boolean isFeedbacked = feedbackQueryRepository.findByPlanIdAndPeriod(planId, current, periodInstant);

		if (isFeedbacked) {
			throw new AjajaException(ErrorCode.ALREADY_FEEDBACK);
		}

		Feedback feedback = Feedback.create(userId, planId, rate, message);
		feedbackRepository.save(feedback);
	}
}
