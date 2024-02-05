package me.ajaja.module.plan.adapter.out.persistence;

import static com.querydsl.core.types.dsl.Expressions.*;
import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;
import static me.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.infra.QAjajaEntity;
import me.ajaja.module.plan.application.port.out.FindPlanDetailPort;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.dto.QPlanResponse_Detail;
import me.ajaja.module.plan.dto.QPlanResponse_Writer;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.tag.domain.QPlanTag;
import me.ajaja.module.tag.domain.QTag;

@Repository
@RequiredArgsConstructor
class FindPlanDetailAdapter implements FindPlanDetailPort {
	private final JPAQueryFactory queryFactory;
	private final PlanMapper planMapper;

	@Override
	public Optional<PlanResponse.Detail> findPlanDetailByIdAndOptionalUser(Long userId, Long id) {
		return Optional.ofNullable(queryFactory.select(new QPlanResponse_Detail(
				new QPlanResponse_Writer(
					userEntity.nickname,
					userId == null ? FALSE : userEntity.id.intValue().eq(asNumber(userId)), // bigint casting error
					userId == null ? FALSE : isAjajaPressed(userId, id)),
				asNumber(id),
				planEntity.title,
				planEntity.description,
				planEntity.iconNumber,
				planEntity.isPublic,
				planEntity.canRemind,
				planEntity.canAjaja,
				planEntity.ajajas.size().longValue(),
				constant(findAllTagsByPlanId(id)),
				planEntity.createdAt))
			.from(planEntity)
			.leftJoin(userEntity).on(userEntity.id.eq(planEntity.userId))
			.where(planEntity.id.eq(id))
			.fetchOne()
		);
	}

	private BooleanExpression isAjajaPressed(Long userId, Long id) {
		return Expressions.asBoolean(queryFactory.selectFrom(QAjajaEntity.ajajaEntity)
			.where(QAjajaEntity.ajajaEntity.targetId.eq(id)
				.and(QAjajaEntity.ajajaEntity.userId.eq(userId))
				.and(QAjajaEntity.ajajaEntity.type.eq(Ajaja.Type.PLAN.name()))
				.and(QAjajaEntity.ajajaEntity.canceled.isFalse()))
			.fetchFirst() != null);
	}

	private List<String> findAllTagsByPlanId(Long planId) {
		return queryFactory.select(QTag.tag.name)
			.from(QPlanTag.planTag, QTag.tag)
			.where(QPlanTag.planTag.tagId.eq(QTag.tag.id)
				.and(QPlanTag.planTag.planId.eq(planId)))
			.fetch();
	}
}
