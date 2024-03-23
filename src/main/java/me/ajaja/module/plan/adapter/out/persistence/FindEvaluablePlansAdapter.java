package me.ajaja.module.plan.adapter.out.persistence;

import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.plan.application.port.out.FindEvaluablePlansPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.mapper.PlanMapper;

@Repository
@RequiredArgsConstructor
public class FindEvaluablePlansAdapter implements FindEvaluablePlansPort {
	private final JPAQueryFactory queryFactory;
	private final PlanMapper mapper;

	@Override
	public List<Plan> findEvaluablePlansByUserIdAndTime(Long id, BaseTime now) {
		return queryFactory.selectFrom(planEntity)
			.where(planEntity.userId.eq(id)
				.and(planEntity.createdAt.year().eq(now.getYear())))
			.fetch()
			.stream()
			.map(mapper::toDomain)
			.toList();
	}
}
