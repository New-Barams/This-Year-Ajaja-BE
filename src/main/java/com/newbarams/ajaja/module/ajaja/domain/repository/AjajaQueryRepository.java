package com.newbarams.ajaja.module.ajaja.domain.repository;

import static com.newbarams.ajaja.module.ajaja.domain.QAjaja.*;
import static com.newbarams.ajaja.module.plan.domain.QPlan.*;
import static com.newbarams.ajaja.module.user.domain.QUser.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.ajaja.domain.Ajaja;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AjajaQueryRepository {
	private final JPAQueryFactory queryFactory;

	public List<Tuple> findRemindableAjaja() {
		return queryFactory.select(
				user.email.email.count(),
				user.email.email
			).from(ajaja)
			.join(plan).on(ajaja.targetId.eq(plan.id))
			.join(user).on(plan.userId.eq(user.id))
			.where(ajaja.updatedAt.after(Instant.now().minus(7, ChronoUnit.DAYS))
				.and(ajaja.updatedAt.before(Instant.now()))
				.and(ajaja.type.eq(Ajaja.Type.PLAN))
				.and(ajaja.isCanceled.eq(false))
				.and(plan.status.isDeleted.eq(false))
			)
			.groupBy(
				user.email.email
			)
			.fetch();
	}
}
