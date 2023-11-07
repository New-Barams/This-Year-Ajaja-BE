package com.newbarams.ajaja.module.feedback.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

@Service
@Transactional(readOnly = true)
public class GetTotalAchieveService {
	private final FeedbackRepository feedbackRepository;

	public GetTotalAchieveService(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}

	public int loadTotalAchieve(Long userId) {
		List<Feedback> userFeedbackList = feedbackRepository.findAllByUserIdAndCreatedYear(userId);

		return (int)userFeedbackList.stream()
			.mapToInt(f -> f.getAchieve().getRate())
			.average()
			.orElse(0);
	}
}
