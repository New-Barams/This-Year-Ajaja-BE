package com.newbarams.ajaja.module.feedback.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadFeedbackService {
	private final FeedbackRepository feedbackRepository;

	public List<Feedback> loadFeedback(Long planId) {
		return feedbackRepository.findAllByPlanIdIdAndCreatedYear(planId);
	}
}
