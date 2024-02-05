package me.ajaja.module.feedback.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.module.feedback.domain.Feedback;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.domain.FeedbackRepository;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.remind.application.port.out.FindPlanRemindQuery;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateFeedbackService {
	private final FindPlanRemindQuery findPlanRemindQuery;
	private final FeedbackQueryRepository feedbackQueryRepository;
	private final FeedbackRepository feedbackRepository;

	public void updateFeedback(Long userId, Long planId, int rate, String message) {
		Plan plan = findPlanRemindQuery.loadByUserIdAndPlanId(userId, planId);
		TimeValue now = TimeValue.now();

		TimeValue period = plan.getFeedbackPeriod(now);
		checkExistFeedback(planId, period);

		Feedback feedback = Feedback.create(userId, planId, rate, message);
		feedbackRepository.save(feedback);
	}

	private void checkExistFeedback(Long planId, TimeValue period) {
		if (feedbackQueryRepository.existByPlanIdAndPeriod(planId, period)) {
			throw new AjajaException(ErrorCode.ALREADY_FEEDBACK);
		}
	}
}
