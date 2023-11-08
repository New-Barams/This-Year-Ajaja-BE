package com.newbarams.ajaja.module.feedback.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateFeedbackService {
	private final FeedbackRepository feedbackRepository;

	@Transactional
	public void updateFeedback(Long feedbackId, int rate) throws IllegalAccessException {
		Feedback feedback = feedbackRepository.findById(feedbackId)
			.orElseThrow(NullPointerException::new);

		feedback.checkDeadline();

		feedback.updateAchieve(rate);
		feedbackRepository.save(feedback);
	}
}
