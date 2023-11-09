package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPlanAchieveService {

	private final FeedbackRepository feedbackRepository;

	public int loadPlanAchieve(Long planId) {
		return (int)feedbackRepository.findAllByPlanIdIdAndCreatedYear(planId)
			.stream()
			.mapToInt(f -> f.getAchieve().getRate())
			.average()
			.orElse(0);
	}
}
