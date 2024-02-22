package me.ajaja.module.feedback.infra;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.feedback.domain.Feedback;
import me.ajaja.module.feedback.domain.FeedbackRepository;
import me.ajaja.module.feedback.mapper.FeedbackMapper;

@Repository
@RequiredArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepository {
	private final FeedbackJpaRepository feedbackJpaRepository;
	private final FeedbackMapper mapper;

	@Override
	public void save(Feedback feedback) {
		feedbackJpaRepository.save(mapper.toEntity(feedback));
	}
}
