package com.newbarams.ajaja.module.feedback.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetTotalAchieveService {
	private final FeedbackRepository feedbackRepository;

	public int loadTotalAchieve(Long userId) {
		return (int)feedbackRepository.findAllByUserIdAndCreatedYear(userId)
			.stream()
			.mapToInt(Feedback::getRate)
			.average()
			.orElse(0);
	}
}
