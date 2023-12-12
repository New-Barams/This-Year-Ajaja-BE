package com.newbarams.ajaja.module.feedback.infra;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackRepository;
import com.newbarams.ajaja.module.feedback.mapper.FeedbackMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepository {
	private final FeedbackJpaRepository feedbackJpaRepository;
	private final FeedbackMapper mapper;

	@Override
	public Feedback save(Feedback feedback) {
		FeedbackEntity entity = feedbackJpaRepository.save(mapper.toEntity(feedback));
		return mapper.toDomain(entity);
	}
}
