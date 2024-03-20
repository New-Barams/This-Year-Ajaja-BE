package me.ajaja.module.plan.adapter.out.persistence;

import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.feedback.application.FindUpdatableTargetService;
import me.ajaja.module.feedback.application.model.UpdatableFeedback;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.mapper.PlanMapper;

@Repository
@RequiredArgsConstructor
public class FindUpdatableTargetsAdapter implements FindUpdatableTargetService {
	private final JPAQueryFactory queryFactory;
	private final PlanMapper mapper;

	@Override
	public List<UpdatableFeedback> findUpdatableTargetsByUserId(Long userId) {
		BaseTime now = BaseTime.now();
		List<Plan> plans = queryFactory.selectFrom(planEntity)
			.where(planEntity.userId.eq(userId)
				.and(planEntity.createdAt.year().eq(now.getYear())))
			.fetch()
			.stream()
			.map(mapper::toDomain)
			.toList();

		return getUpdatablePlans(now, plans);
	}

	private List<UpdatableFeedback> getUpdatablePlans(BaseTime now, List<Plan> plans) {
		return plans.stream()
			.filter(plan -> plan.isWithinPeriod(now))
			.map(mapper::toFeedbackModel)
			.toList();
	}
}
