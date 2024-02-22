package me.ajaja.module.plan.adapter.out.persistence;

import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;
import static me.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.plan.adapter.out.persistence.model.PlanEntity;
import me.ajaja.module.plan.application.port.out.FindAllPlansQuery;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.tag.domain.QPlanTag;
import me.ajaja.module.tag.domain.QTag;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindAllPlansAdapter implements FindAllPlansQuery {
	private static final String LATEST = "latest";
	private static final String AJAJA = "ajaja";
	private static final int PAGE_SIZE = 3;

	private final JPAQueryFactory queryFactory;
	private final PlanMapper planMapper;

	@Override
	public List<PlanResponse.GetAll> findAllByCursorAndSorting(PlanRequest.GetAll conditions) {
		List<Tuple> tuples = queryFactory.select(planEntity, userEntity.nickname)
			.from(planEntity)

			.leftJoin(userEntity).on(userEntity.id.eq(planEntity.userId))

			.where(planEntity.isPublic.eq(true),
				isEqualsYear(conditions.isCurrent()),
				cursorPagination(conditions))

			.orderBy(sortBy(conditions.getSort()))
			.limit(PAGE_SIZE)
			.fetch();

		return tupleToResponse(tuples);
	}

	private BooleanExpression isEqualsYear(boolean isNewYear) {
		int currentYear = TimeValue.now().getYear();
		return isNewYear ? planEntity.createdAt.year().eq(currentYear) : planEntity.createdAt.year().ne(currentYear);
	}

	private BooleanExpression cursorPagination(PlanRequest.GetAll conditions) {
		return conditions.getStart() == null ? null :
			getCursorCondition(conditions.getSort(), conditions.getStart(), conditions.getAjaja());
	}

	private BooleanExpression getCursorCondition(String sort, Long start, Integer cursorAjaja) {
		return sort.equalsIgnoreCase(LATEST) ? planEntity.id.lt(start) : cursorAjajaAndId(cursorAjaja, start);
	}

	private BooleanExpression cursorAjajaAndId(Integer cursorAjaja, Long cursorId) {
		return cursorAjaja == null ? null :
			planEntity.ajajas.size().eq(cursorAjaja)
				.and(planEntity.id.lt(cursorId))
				.or(planEntity.ajajas.size().lt(cursorAjaja));
	}

	private OrderSpecifier[] sortBy(String condition) {
		return switch (condition.toLowerCase(Locale.ROOT)) {
			case LATEST -> new OrderSpecifier[] {new OrderSpecifier<>(Order.DESC, planEntity.createdAt)};
			case AJAJA -> new OrderSpecifier[] {
				new OrderSpecifier<>(Order.DESC, planEntity.ajajas.size()),
				new OrderSpecifier<>(Order.DESC, planEntity.id)
			};
			default -> new OrderSpecifier[0];
		};
	}

	private List<PlanResponse.GetAll> tupleToResponse(List<Tuple> tuples) {
		return tuples.stream()
			.map(tuple -> {
				PlanEntity planFromTuple = tuple.get(planEntity);
				String nickname = tuple.get(userEntity.nickname);
				List<String> tags = findAllTagsByPlanId(planFromTuple.getId());

				return planMapper.toResponse(planFromTuple, nickname, tags);
			})
			.toList();
	}

	private List<String> findAllTagsByPlanId(Long planId) {
		return queryFactory.select(QTag.tag.name)
			.from(QPlanTag.planTag, QTag.tag)
			.where(QPlanTag.planTag.tagId.eq(QTag.tag.id)
				.and(QPlanTag.planTag.planId.eq(planId)))
			.fetch();
	}

	@Override
	public List<Plan> findAllCurrentPlansByUserId(Long userId) { // todo: domain dependency
		return queryFactory.selectFrom(planEntity)
			.where(planEntity.userId.eq(userId)
				.and(isCurrentYear()))
			.fetch()
			.stream().map(planMapper::toDomain)
			.toList();
	}

	private BooleanExpression isCurrentYear() {
		return planEntity.createdAt.year().eq(TimeValue.now().getYear());
	}
}
