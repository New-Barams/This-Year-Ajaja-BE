package com.newbarams.ajaja.module.feedback.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.domain.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateFeedbackService {
	private final FeedbackQueryRepository feedbackQueryRepository;
	private final FeedbackRepository feedbackRepository;

	public void updateFeedback(Long feedbackId, int rate, String message) {
		Feedback feedback = feedbackQueryRepository.findByFeedbackId(feedbackId)
			.orElseThrow(() -> AjajaException.withId(feedbackId, NOT_FOUND_FEEDBACK));

		feedback.updateFeedback(rate, message);
		feedbackRepository.save(feedback);
	}
}
