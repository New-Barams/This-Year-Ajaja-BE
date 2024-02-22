package me.ajaja.module.feedback.infra;

import static me.ajaja.module.feedback.infra.QFeedbackEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.feedback.domain.Feedback;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.infra.model.AchieveInfo;
import me.ajaja.module.feedback.infra.model.FeedbackInfo;
import me.ajaja.module.feedback.mapper.FeedbackMapper;

@Repository
@RequiredArgsConstructor
class FeedbackQueryRepositoryImpl implements FeedbackQueryRepository {
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
	public boolean existByPlanIdAndPeriod(Long planId, TimeValue period) {
		return queryFactory.selectFrom(feedbackEntity)
			.where(feedbackEntity.planId.eq(planId)
				.and(feedbackEntity.createdAt.between(period.getInstant(), period.oneMonthLater().toInstant()))
			).fetchFirst() != null;
	}

	@Override
	public List<FeedbackInfo> findFeedbackInfosByPlanId(Long planId) {
		return queryFactory.select(Projections.constructor(FeedbackInfo.class,
					feedbackEntity.id,
					isFeedback(),
					feedbackEntity.achieve,
					feedbackEntity.message,
					feedbackEntity.createdAt.month(),
					feedbackEntity.createdAt.dayOfMonth()
				)
			).from(feedbackEntity)
			.where(feedbackEntity.planId.eq(planId))
			.orderBy(feedbackEntity.createdAt.month().asc())
			.fetch();
	}

	private BooleanExpression isFeedback() {
		return new CaseBuilder()
			.when(feedbackEntity.updatedAt.after(feedbackEntity.createdAt))
			.then(true)
			.otherwise(false);
	}

	@Override
	public List<AchieveInfo> findAchievesByUserIdAndYear(Long userId, int year) {
		return queryFactory.select(Projections.constructor(AchieveInfo.class,
				feedbackEntity.planId, feedbackEntity.achieve))
			.from(feedbackEntity)
			.where(feedbackEntity.userId.eq(userId)
				.and(feedbackEntity.createdAt.year().eq(year)))
			.fetch();
	}
}
