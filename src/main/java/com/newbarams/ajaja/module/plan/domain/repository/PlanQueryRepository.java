package com.newbarams.ajaja.module.plan.domain.repository;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;
import static com.newbarams.ajaja.module.ajaja.domain.QAjaja.*;
import static com.newbarams.ajaja.module.plan.domain.QPlan.*;
import static com.newbarams.ajaja.module.tag.domain.QPlanTag.*;
import static com.newbarams.ajaja.module.tag.domain.QTag.*;
import static com.newbarams.ajaja.module.user.domain.QUser.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.ajaja.domain.Ajaja;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.remind.domain.dto.Response;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlanQueryRepository {
	private static final List<Integer> MONTHS_PER_ONE_MONTH = new ArrayList<>(List.of(2, 4, 5, 7, 8, 10, 11));
	private static final List<Integer> MONTHS_PER_THREE_MONTH = new ArrayList<>(List.of(3, 9));
	private static final List<Integer> MONTHS_PER_SIX_MONTH = new ArrayList<>(List.of(6));
	private final Instant instant = Instant.now();
	private final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
	private static final String AJAJA = "Ajaja";
	private static final String CREATED_AT = "CreatedAt";

	private final JPAQueryFactory queryFactory;

	public PlanResponse.GetOne findById(Long id, Long userId) {
		List<Tuple> tuples = queryFactory.select(plan, user.nickname)
			.from(plan, user)
			.where(plan.userId.eq(user.id).and(plan.id.eq(id)))
			.fetch();

		validateTuple(tuples);
		return tupleToResponse(tuples.get(0), userId);
	}

	private void validateTuple(List<Tuple> tuples) {
		if (tuples.isEmpty()) {
			throw new AjajaException(NOT_FOUND_PLAN);
		}
	}

	private PlanResponse.GetOne tupleToResponse(Tuple tuple, Long userId) {
		Plan planFromTuple = tuple.get(plan);
		String nickname = tuple.get(user.nickname).getNickname();

		List<String> tags = findTagByPlanId(planFromTuple.getId());
		Long ajajas = countNotCanceledAjaja(planFromTuple.getId());
		boolean isPressAjaja = isPressAjaja(planFromTuple, userId);

		return PlanMapper.toResponse(planFromTuple, nickname, tags, ajajas, isPressAjaja);
	}

	private boolean isPressAjaja(Plan plan, Long userId) {
		Ajaja ajaja = plan.getAjajaByUserId(userId);

		if (ajaja.isEqualsDefault()) {
			return false;
		}

		return true;
	}

	private List<String> findTagByPlanId(Long planId) {
		return queryFactory.select(tag.name)
			.from(planTag, tag)
			.where(planTag.tagId.eq(tag.id).and(planTag.planId.eq(planId)))
			.fetch();
	}

	private Long countNotCanceledAjaja(Long planId) {
		return queryFactory.select(ajaja.count())
			.from(ajaja)
			.where(ajaja.targetId.eq(planId).and(ajaja.isCanceled.eq(false)))
			.fetchOne();
	}

	public List<PlanResponse.GetAll> findAllByCursorAndSorting(PlanRequest.GetAll conditions) {
		List<Tuple> tuples = queryFactory.select(plan, user.nickname)
			.from(plan, user)

			.where(plan.userId.eq(user.id),
				plan.status.isPublic.eq(true),
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
			.map(tuple -> {
				Plan planFromTuple = tuple.get(plan);
				String nickname = tuple.get(user.nickname).getNickname();
				List<String> tags = findTagByPlanId(planFromTuple.getId());
				Long ajajas = countNotCanceledAjaja(planFromTuple.getId());

				return PlanMapper.toGetAllResponse(planFromTuple, nickname, tags, ajajas);
			})
			.toList();
	}

	public List<PlanInfoResponse.GetPlan> findAllPlanByUserId(Long userId) {
		return queryFactory.select(Projections.constructor(PlanInfoResponse.GetPlan.class,
				plan.createdAt.year(),
				plan.id,
				plan.content.title,
				plan.status.canRemind,
				plan.achieveRate,
				plan.iconNumber,
				user.email.isVerified
			))
			.from(plan, user)
			.groupBy(plan.createdAt.year(),
				plan.id,
				plan.content.title,
				plan.status.canRemind,
				plan.achieveRate,
				plan.iconNumber,
				user.email.isVerified)
			.where(plan.userId.eq(userId))
			.orderBy(plan.createdAt.year().desc())
			.fetch();
	}

	public List<Response> findAllRemindablePlan(String remindTime) {
		return queryFactory.select(Projections.constructor(Response.class,
				user.id,
				plan.id,
				user.email.email,
				new CaseBuilder()
					.when(plan.info.remindTerm.eq(1))
					.then(zonedDateTime.getMonthValue() - 1)
					.otherwise(zonedDateTime.getMonthValue()),
				plan.info.remindTerm,
				plan.info
			))
			.from(plan)
			.join(user).on(plan.userId.eq(user.id))
			.where(plan.status.canRemind
				.and(isCurrentYear())
				.and(isRemindMonth())
				.and(isCurrentDate())
				.and(plan.info.remindTime.stringValue().eq(remindTime))
			)
			.fetch();
	}

	private BooleanExpression isRemindMonth() {
		int month = zonedDateTime.getMonthValue();

		if (MONTHS_PER_ONE_MONTH.contains(month)) {
			return plan.info.remindTerm.eq(1);
		} else if (MONTHS_PER_THREE_MONTH.contains(month)) {
			return plan.info.remindTerm.in(1, 3);
		} else if (MONTHS_PER_SIX_MONTH.contains(month)) {
			return plan.info.remindTerm.in(1, 3, 6);
		}

		return plan.info.remindTerm.in(1, 3, 6, 12);
	}

	private BooleanExpression isCurrentYear() {
		return plan.createdAt.year().eq(zonedDateTime.getYear());
	}

	private BooleanExpression isCurrentDate() {
		return plan.info.remindDate.eq(zonedDateTime.getDayOfMonth());
	}
}
