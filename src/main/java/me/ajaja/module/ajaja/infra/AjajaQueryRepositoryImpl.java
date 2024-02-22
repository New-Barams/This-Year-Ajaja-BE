package me.ajaja.module.ajaja.infra;

import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;
import static me.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.remind.application.model.RemindableAjaja;

@Repository
@RequiredArgsConstructor
public class AjajaQueryRepositoryImpl implements AjajaQueryRepository {
	private final JPAQueryFactory queryFactory;

	public List<RemindableAjaja> findRemindableAjaja() {
		return queryFactory.select(Projections.constructor(RemindableAjaja.class,
				planEntity.title,
				planEntity.id,
				userEntity.id,
				userEntity.remindEmail.count(),
				userEntity.remindEmail
			)).from(QAjajaEntity.ajajaEntity)
			.join(planEntity).on(QAjajaEntity.ajajaEntity.targetId.eq(planEntity.id))
			.join(userEntity).on(planEntity.userId.eq(userEntity.id))
			.where(planEntity.canAjaja.eq(true)
				.and(userEntity.remindType.eq("EMAIL")
					.or(userEntity.remindType.eq("BOTH")))
				.and(QAjajaEntity.ajajaEntity.updatedAt.between(Instant.now().minus(7, ChronoUnit.DAYS), Instant.now()))
				.and(QAjajaEntity.ajajaEntity.type.eq(Ajaja.Type.PLAN.name()))
				.and(QAjajaEntity.ajajaEntity.canceled.isFalse())
				.and(planEntity.deleted.isFalse()
					.and(userEntity.deleted.isFalse()))
			)
			.groupBy(
				planEntity.title,
				planEntity.id,
				userEntity.signUpEmail
			)
			.fetch();
	}
}
