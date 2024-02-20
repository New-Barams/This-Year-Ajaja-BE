package me.ajaja.module.remind.adapter.out.persistence;

import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;
import static me.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.plan.domain.RemindDate;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetPort;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.mapper.RemindMapper;

@Repository
@RequiredArgsConstructor
public class FindRemindableTargetAdapter implements FindRemindableTargetPort {
	private final JPAQueryFactory queryFactory;
	private final PlanMapper planMapper;
	private final RemindMapper mapper;

	@Override
	public List<Remind> findAllRemindablePlan(String remindTime, String remindType, TimeValue time) {
		return queryFactory.select(planEntity, userEntity.remindEmail, userEntity.phoneNumber,
				userEntity.remindType)
			.from(planEntity)
			.join(userEntity).on(userEntity.id.eq(planEntity.userId))
			.where(planEntity.canRemind
				.and(planEntity.remindTime.eq(remindTime).and(isRemindable(time)))
				.and(userEntity.remindType.eq(remindType).or(userEntity.remindType.eq("BOTH")))
			)
			.fetch().stream()
			.map(t -> planMapper.toModel(
				t.get(planEntity),
				t.get(userEntity.remindType),
				t.get(userEntity.remindEmail),
				t.get(userEntity.phoneNumber)))
			.map(info -> mapper.toDomain(info, time))
			.toList();
	}

	private BooleanExpression isRemindable(TimeValue time) {
		RemindDate today = new RemindDate(time.getMonth(), time.getDate());
		return planEntity.createdAt.year().eq(time.getYear())
			.andAnyOf(planEntity.messages.any().remindDate.eq(today));
	}
}