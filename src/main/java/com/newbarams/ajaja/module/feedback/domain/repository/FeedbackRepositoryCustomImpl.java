package com.newbarams.ajaja.module.feedback.domain.repository;

import static com.newbarams.ajaja.module.feedback.domain.QFeedback.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedbackRepositoryCustomImpl implements FeedbackRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Feedback> findAllByUserIdAndCreatedYear(Long userId) {

		int currentYear = getCurrentYear();

		return queryFactory.select(feedback)
			.from(feedback)
			.where(feedback.userId.eq(userId).and(feedback.createdAt.year().eq(currentYear)))
			.fetch();
	}

	private int getCurrentYear() {
		Instant instant = Instant.now();
		ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
		return zonedDateTime.getYear();
	}
}
