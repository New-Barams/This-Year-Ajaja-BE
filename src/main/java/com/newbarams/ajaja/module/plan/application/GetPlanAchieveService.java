package com.newbarams.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

@Service
@Transactional(readOnly = true)
public class GetPlanAchieveService {

	private final FeedbackRepository feedbackRepository;

	public GetPlanAchieveService(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}

	public int loadPlanAchieve(Long planId) {
		List<Feedback> feedbackList = feedbackRepository.findAllByPlanIdIdAndCreatedYear(planId);

		return (int)feedbackList.stream().mapToInt(f -> f.getAchieve().getRate()).average().orElse(0);
	}
}
