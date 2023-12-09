package com.newbarams.ajaja.module.ajaja.domain.repository;

import static com.newbarams.ajaja.module.ajaja.domain.QAjaja.*;
import static com.newbarams.ajaja.module.plan.domain.QPlan.*;
import static com.newbarams.ajaja.module.user.infra.QUserEntity.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.ajaja.domain.Ajaja;
import com.newbarams.ajaja.module.remind.application.model.RemindableAjaja;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AjajaQueryRepositoryImpl implements AjajaQueryRepository {
	private final JPAQueryFactory queryFactory;

	public List<RemindableAjaja> findRemindableAjaja() {
		return queryFactory.select(Projections.constructor(RemindableAjaja.class,
				plan.content.title,
				plan.id,
				userEntity.remindEmail.count(),
				userEntity.remindEmail
			)).from(ajaja)
			.join(plan).on(ajaja.targetId.eq(plan.id))
			.join(userEntity).on(plan.userId.eq(userEntity.id))
			.where(plan.status.canAjaja.eq(true)
				.and(ajaja.updatedAt.after(Instant.now().minus(7, ChronoUnit.DAYS)))
				.and(ajaja.updatedAt.before(Instant.now()))
				.and(ajaja.type.eq(Ajaja.Type.PLAN))
				.and(ajaja.isCanceled.eq(false))
				.and(plan.status.isDeleted.eq(false))
			)
			.groupBy(
				plan.content.title,
				plan.id,
				userEntity.signUpEmail
			)
			.fetch();
	}
}
