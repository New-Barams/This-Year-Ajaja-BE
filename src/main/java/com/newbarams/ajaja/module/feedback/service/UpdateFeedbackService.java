package com.newbarams.ajaja.module.feedback.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

@Service
@Transactional(readOnly = true)
public class UpdateFeedbackService {
	private final FeedbackRepository feedbackRepository;

	public UpdateFeedbackService(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}

	@Transactional
	public void updateFeedback(Long feedbackId, int rate) throws IllegalAccessException {
		Feedback feedback = feedbackRepository.findById(feedbackId)
			.orElseThrow(NullPointerException::new);

		checkDeadline(feedback.getCreatedAt());

		feedback.updateAchieve(rate);
		feedbackRepository.save(feedback);
	}

	private void checkDeadline(Instant remindDate) throws IllegalAccessException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Timestamp deadline = Timestamp.from(remindDate.plus(1, ChronoUnit.MONTHS));

		boolean isInvalidFeedback = timestamp.before(Timestamp.from(remindDate)) || timestamp.after(deadline);

		if (isInvalidFeedback) {
			throw new IllegalAccessException("평가 기간이 지났습니다.");
		}
	}
}
