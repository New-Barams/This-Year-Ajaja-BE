package com.newbarams.ajaja.module.feedback.infra;

import static com.newbarams.ajaja.module.feedback.infra.QFeedbackEntity.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.mapper.FeedbackMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedbackQueryRepositoryImpl implements FeedbackQueryRepository {
	private final JPAQueryFactory queryFactory;
	private final FeedbackMapper mapper;

	@Override
	public List<Feedback> findAllFeedbackByPlanId(Long planId) {
		List<FeedbackEntity> entities = queryFactory.selectFrom(feedbackEntity)
			.where(feedbackEntity.planId.eq(planId))
			.orderBy(feedbackEntity.createdAt.asc())
			.fetch();

		return mapper.toDomain(entities);
	}

	@Override
	public Optional<Feedback> findByFeedbackId(Long feedbackId) {
		FeedbackEntity entity = queryFactory.selectFrom(feedbackEntity)
			.where(feedbackEntity.id.eq(feedbackId))
			.fetchOne();

		return Optional.of(mapper.toDomain(entity));
	}

	@Override
	public void update(Long feedbackId, String achieve) {
		queryFactory.update(feedbackEntity)
			.set(feedbackEntity.achieve, achieve)
			.set(feedbackEntity.updatedAt, new TimeValue().now())
			.where(feedbackEntity.id.eq(feedbackId))
			.execute();
	}
}
