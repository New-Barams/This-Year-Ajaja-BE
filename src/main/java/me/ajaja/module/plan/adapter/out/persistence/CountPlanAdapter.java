package me.ajaja.module.plan.adapter.out.persistence;

import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.plan.application.port.out.CountPlanPort;

@Repository
@RequiredArgsConstructor
class CountPlanAdapter implements CountPlanPort {
	private final JPAQueryFactory queryFactory;

	@Override
	public Long countByUserId(Long userId) {
		return queryFactory.select(planEntity.count())
			.from(planEntity)
			.where(planEntity.userId.eq(userId)
				.and(isCurrentYear()))
			.fetchFirst();
	}

	private BooleanExpression isCurrentYear() {
		return planEntity.createdAt.year().eq(BaseTime.now().getYear());
	}
}
