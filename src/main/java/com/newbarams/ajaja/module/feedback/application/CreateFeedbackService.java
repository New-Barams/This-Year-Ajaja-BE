package com.newbarams.ajaja.module.feedback.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateFeedbackService {
	private final FeedbackRepository feedbackRepository;

	public void create(Long userId, Long planId) {
		Feedback feedback = Feedback.create(userId, planId);
		feedbackRepository.save(feedback);
	}
}
