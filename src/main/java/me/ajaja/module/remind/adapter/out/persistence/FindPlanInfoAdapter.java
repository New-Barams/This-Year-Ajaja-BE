package me.ajaja.module.remind.adapter.out.persistence;

import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.feedback.infra.QFeedbackEntity;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.remind.application.port.out.FindPlanInfoPort;

@Repository
@RequiredArgsConstructor
public class FindPlanInfoAdapter implements FindPlanInfoPort {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<PlanResponse.PlanInfo> findAllPlanInfosByUserId(Long userId) {
		return queryFactory.select(Projections.constructor(PlanResponse.PlanInfo.class,
				planEntity.createdAt.year(),
				planEntity.id,
				planEntity.title,
				planEntity.canRemind,
				QFeedbackEntity.feedbackEntity.achieve.avg().intValue(),
				planEntity.iconNumber
			))
			.from(planEntity)
			.leftJoin(QFeedbackEntity.feedbackEntity).on(QFeedbackEntity.feedbackEntity.planId.eq(planEntity.id))
			.groupBy(planEntity.createdAt.year(),
				planEntity.id,
				planEntity.title,
				planEntity.canRemind,
				planEntity.iconNumber)
			.where(planEntity.userId.eq(userId))
			.orderBy(planEntity.createdAt.year().desc())
			.fetch();
	}
}
