package com.newbarams.ajaja.module.plan.repository;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;
import static com.newbarams.ajaja.module.plan.domain.QPlan.*;
import static com.newbarams.ajaja.module.user.domain.QUser.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlanQueryRepository {
	private static final String AJAJA = "Ajaja";
	private static final String CREATED_AT = "CreatedAt";

	private final JPAQueryFactory queryFactory;

	public PlanResponse.GetOne findById(Long id) {
		List<Tuple> tuples = queryFactory.select(plan, user.nickname)
			.from(plan, user)
			.where(plan.userId.eq(user.id).and(plan.id.eq(id)))
			.fetch();

		validateTuple(tuples);
		return tupleToResponse(tuples.get(0));
	}

	private void validateTuple(List<Tuple> tuples) {
		if (tuples.isEmpty()) {
			throw new NoSuchElementException(NOT_FOUND_PLAN.getMessage());
		}
	}

	private PlanResponse.GetOne tupleToResponse(Tuple tuple) {
		Plan planFromTuple = tuple.get(plan);
		String nickname = tuple.get(user.nickname).getNickname();

		return PlanMapper.toResponse(planFromTuple, nickname);
	}

	public List<PlanResponse.GetAll> findAllByCursorAndSorting(PlanRequest.GetAll conditions) {
		List<Tuple> tuples = queryFactory.select(plan, user.nickname)
			.from(plan, user)

			.where(plan.userId.eq(user.id),
				isEqualsYear(conditions.isNewYear()),
				cursorCreatedAtAndId(conditions.cursorCreatedAt(), conditions.cursorId()))

			.orderBy(sortBy(conditions.sortCondition()))
			.limit(conditions.pageSize())
			.fetch();

		return tupleToResponse(tuples);
	}

	private BooleanExpression isEqualsYear(boolean isNewYear) {
		int currentYear = Instant.now()
			.atZone(ZoneId.systemDefault())
			.getYear();

		if (isNewYear) {
			return plan.createdAt.year().eq(currentYear);
		}

		return plan.createdAt.year().eq(currentYear).not();
	}

	private BooleanExpression cursorCreatedAtAndId(Instant cursorCreatedAt, Long cursorId) {
		if (cursorCreatedAt == null || cursorId == null) {
			return null;
		}

		return plan.createdAt.eq(cursorCreatedAt)
			.and(plan.id.lt(cursorId))
			.or(plan.createdAt.lt(cursorCreatedAt));
	}

	private OrderSpecifier<?> sortBy(String sortCondition) {
		if (sortCondition == null) {
			return null;
		}

		if (sortCondition.equalsIgnoreCase(CREATED_AT)) {
			return new OrderSpecifier(Order.DESC, plan.createdAt);
		}

		if (sortCondition.equalsIgnoreCase(AJAJA)) {
			return new OrderSpecifier(Order.DESC, plan.ajajas.size());
		}

		return null;
	}

	private List<PlanResponse.GetAll> tupleToResponse(List<Tuple> tuples) {
		return tuples.stream()
			.map((tuple) -> PlanMapper.toGetAllResponse(tuple.get(plan), tuple.get(user.nickname).getNickname()))
			.toList();
	}

	public List<PlanInfoResponse.GetGetPlan> findAllPlanByUserId(Long userId) {
		return queryFactory.select(Projections.bean(PlanInfoResponse.GetGetPlan.class,
				plan.content.title,
				plan.status.canRemind,
				plan.achieveRate))
			.from(plan)
			.where(plan.userId.eq(userId)
				.and(isCurrentYear()))
			.fetch();
	}

	private BooleanExpression isCurrentYear() {
		Instant instant = Instant.now();
		ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

		return plan.createdAt.year().eq(zonedDateTime.getYear());
	}
}
