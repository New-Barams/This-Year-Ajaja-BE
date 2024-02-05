package me.ajaja.module.remind.adapter.out.persistence;

import static me.ajaja.global.exception.ErrorCode.*;
import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.adapter.out.persistence.model.PlanEntity;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.remind.application.port.out.FindPlanRemindQuery;
import me.ajaja.module.remind.dto.RemindResponse;
import me.ajaja.module.remind.mapper.RemindInfoMapper;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindPlanRemindAdapter implements FindPlanRemindQuery {
	private final JPAQueryFactory queryFactory;
	private final RemindInfoMapper mapper;
	private final PlanMapper planMapper;

	@Override
	public RemindResponse.RemindInfo findByUserIdAndPlanId(Long userId, Long planId) {
		Plan plan = loadByUserIdAndPlanId(userId, planId);

		List<RemindResponse.Message> messages = plan.getMessages()
			.stream().map(mapper::toMessage)
			.toList();

		return mapper.toRemindInfo(plan, messages);
	}

	@Override
	public Plan loadByUserIdAndPlanId(Long userId, Long planId) {
		PlanEntity entity = queryFactory.selectFrom(planEntity)
			.where(planEntity.userId.eq(userId)
				.and(planEntity.id.eq(planId)))
			.fetchOne();

		if (entity == null) {
			throw AjajaException.withId(planId, NOT_FOUND_PLAN);
		}
		return planMapper.toDomain(entity);
	}
}
