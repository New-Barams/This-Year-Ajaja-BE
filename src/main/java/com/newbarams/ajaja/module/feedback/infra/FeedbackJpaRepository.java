package com.newbarams.ajaja.module.feedback.infra;

import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.mapper.FeedbackEntityMapper;

public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {
	FeedbackEntityMapper mapper = Mappers.getMapper(FeedbackEntityMapper.class);

	default void save(Feedback feedback) {
		FeedbackEntity feedbackEntity = mapper.mapEntityFrom(feedback);
		save(feedbackEntity);
	}
}
