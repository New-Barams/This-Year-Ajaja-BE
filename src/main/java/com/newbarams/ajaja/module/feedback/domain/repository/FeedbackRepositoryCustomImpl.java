package com.newbarams.ajaja.module.feedback.domain.repository;

import static com.newbarams.ajaja.module.feedback.domain.QFeedback.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedbackRepositoryCustomImpl implements FeedbackRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Feedback> findAllByPlanIdIdAndCreatedYear(Long planId) {
		return queryFactory.selectFrom(feedback)
			.where(feedback.planId.eq(planId).and(isCurrentYear()))
			.orderBy(feedback.createdAt.asc())
			.fetch();
	}

	private BooleanExpression isCurrentYear() {
		Instant instant = Instant.now();
		ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
		return feedback.createdAt.year().eq(zonedDateTime.getYear());
	}
}
