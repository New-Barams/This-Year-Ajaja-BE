package com.newbarams.ajaja.module.remind.repository;

import static com.newbarams.ajaja.module.remind.domain.QRemind.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.remind.domain.Remind;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RemindQueryRepository {
	private final JPAQueryFactory queryFactory;

	public List<Remind> findRemindByHour(int remindHour) {
		return queryFactory
			.selectFrom(remind)
			.where(remind.type.eq(Remind.Type.PLAN)
				.and(remind.period.start.hour().eq(remindHour)))
			.fetch();
	}

	private BooleanExpression isCurrentYear() {
		return remind.createdAt.year().eq(getInstantTime().getYear());
	}

	private BooleanExpression isCurrentMonth() {
		return remind.createdAt.month().eq(getInstantTime().getMonthValue());
	}

	private BooleanExpression isCurrentDate() {
		return remind.createdAt.dayOfMonth().eq(getInstantTime().getDayOfMonth());
	}

	private ZonedDateTime getInstantTime() {
		Instant instant = Instant.now();
		return instant.atZone(ZoneId.systemDefault());
	}
}
