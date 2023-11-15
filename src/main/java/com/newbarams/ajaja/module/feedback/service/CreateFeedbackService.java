package com.newbarams.ajaja.module.feedback.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateFeedbackService {
	private final FeedbackRepository feedbackRepository;

	public Long createFeedback(Long userId, Long planId) {
		Feedback feedback = Feedback.create(userId, planId);
		Feedback saved = feedbackRepository.save(feedback);

		return saved.getId();
	}
}
