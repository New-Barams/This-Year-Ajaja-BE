package com.newbarams.ajaja.module.plan.domain.repository;

import static com.newbarams.ajaja.global.util.QueryDslUtil.*;
import static com.newbarams.ajaja.module.ajaja.domain.Ajaja.Type.*;
import static com.newbarams.ajaja.module.ajaja.domain.QAjaja.*;
import static com.newbarams.ajaja.module.plan.domain.QPlan.*;
import static com.newbarams.ajaja.module.tag.domain.QPlanTag.*;
import static com.newbarams.ajaja.module.tag.domain.QTag.*;
import static com.newbarams.ajaja.module.user.infra.QUserEntity.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.ajaja.domain.Ajaja;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.remind.dto.RemindMessageInfo;
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
	private static final String LATEST = "latest";
	private static final String AJAJA = "ajaja";
	private static final int PAGE_SIZE = 3;

	private final JPAQueryFactory queryFactory;

	public List<Plan> findAllCurrentPlansByUserId(Long userId) {
		return queryFactory.selectFrom(plan)
			.where(plan.userId.eq(userId)
				.and(isCurrentYear()))
			.fetch();
	}

	public Optional<PlanResponse.GetOne> findById(Long id, Long userId) {
		List<Tuple> tuples = queryFactory.select(plan, userEntity.nickname)
			.from(plan, userEntity)
			.where(plan.userId.eq(userEntity.id).and(plan.id.eq(id)))
			.fetch();

		return getResponse(tuples, userId);
	}

	private Optional<PlanResponse.GetOne> getResponse(List<Tuple> tuples, Long userId) {
		if (tuples.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(tupleToResponse(tuples.get(0), userId));
	}

	private PlanResponse.GetOne tupleToResponse(Tuple tuple, Long userId) {
		Plan planFromTuple = tuple.get(plan);
		String nickname = tuple.get(userEntity.nickname);

		List<String> tags = findTagByPlanId(planFromTuple.getId());
		boolean isPressAjaja = isPressAjaja(planFromTuple.getId(), userId);

		return PlanMapper.toResponse(planFromTuple, nickname, tags, isPressAjaja);
	}

	private boolean isPressAjaja(Long planId, Long userId) {
		List<Ajaja> ajajas = queryFactory.selectFrom(ajaja)
			.where(ajaja.targetId.eq(planId)
				.and(ajaja.userId.eq(userId))
				.and(ajaja.type.eq(PLAN))
				.and(ajaja.isCanceled.eq(false)))
			.fetch();

		if (ajajas.isEmpty()) {
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

	public List<PlanResponse.GetAll> findAllByCursorAndSorting(PlanRequest.GetAll conditions) {
		List<Tuple> tuples = queryFactory.select(plan, userEntity.nickname)
			.from(plan, userEntity)

			.where(plan.userId.eq(userEntity.id),
				plan.status.isPublic.eq(true),
				isEqualsYear(conditions.current()),
				cursorPagination(conditions))

			.orderBy(sortBy(conditions.sort()))
			.limit(PAGE_SIZE)
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

	private BooleanExpression cursorPagination(PlanRequest.GetAll conditions) {
		if (conditions.start() == null) {
			return null;
		}

		return getCursorCondition(conditions.sort(), conditions.start(), conditions.ajaja());
	}

	private BooleanExpression getCursorCondition(String sort, Long start, Integer cursorAjaja) {
		if (sort.equalsIgnoreCase(LATEST)) {
			return cursorId(start);
		}

		return cursorAjajaAndId(cursorAjaja, start);
	}

	private BooleanExpression cursorId(Long cursorId) {
		return plan.id.lt(cursorId);
	}

	private BooleanExpression cursorAjajaAndId(Integer cursorAjaja, Long cursorId) {
		if (cursorAjaja == null) {
			return null;
		}

		return plan.ajajas.size().eq(cursorAjaja)
			.and(plan.id.lt(cursorId))
			.or(plan.ajajas.size().lt(cursorAjaja));
	}

	private OrderSpecifier[] sortBy(String condition) {
		List<OrderSpecifier> orders = new ArrayList<>();

		switch (condition.toLowerCase(Locale.ROOT)) {
			case LATEST -> orders.add(new OrderSpecifier<>(Order.DESC, plan.createdAt));
			case AJAJA -> {
				orders.add(new OrderSpecifier<>(Order.DESC, plan.ajajas.size()));
				orders.add(new OrderSpecifier<>(Order.DESC, plan.id));
			}
		}

		return orders.toArray(new OrderSpecifier[orders.size()]);
	}

	private List<PlanResponse.GetAll> tupleToResponse(List<Tuple> tuples) {
		return tuples.stream()
			.map(tuple -> {
				Plan planFromTuple = tuple.get(plan);
				String nickname = tuple.get(userEntity.nickname);
				List<String> tags = findTagByPlanId(planFromTuple.getId());

				return PlanMapper.toGetAllResponse(planFromTuple, nickname, tags);
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
				userEntity.verified
			))
			.from(plan)
			.join(userEntity).on(userEntity.id.eq(plan.userId))
			.groupBy(plan.createdAt.year(),
				plan.id,
				plan.content.title,
				plan.status.canRemind,
				plan.achieveRate,
				plan.iconNumber,
				userEntity.verified)
			.where(plan.userId.eq(userId))
			.orderBy(plan.createdAt.year().desc())
			.fetch();
	}

	public List<RemindMessageInfo> findAllRemindablePlan(String remindTime) {
		List<Tuple> remindablePlans = queryFactory.select(plan, userEntity)
			.from(plan, userEntity)
			.where(plan.status.canRemind
				.and(isCurrentYear())
				.and(isRemindMonth())
				.and(isRemindDate())
				.and(plan.info.remindTime.stringValue().eq(remindTime)
					.and(plan.userId.eq(userEntity.id)))
			)
			.fetch();

		return mapRemindMessageInfos(remindablePlans);
	}

	private BooleanExpression isCurrentYear() {
		return plan.createdAt.year().eq(new TimeValue().getYear());
	}

	private BooleanExpression isRemindDate() {
		return plan.info.remindDate.eq(new TimeValue().getDate());
	}

	private List<RemindMessageInfo> mapRemindMessageInfos(List<Tuple> remindablePlans) {
		return remindablePlans.stream().map(
				p -> new RemindMessageInfo(
					p.get(plan).getUserId(),
					p.get(plan).getId(),
					p.get(plan).getContent().getTitle(),
					p.get(userEntity).getRemindEmail(),
					p.get(plan).getMessage(p.get(plan).getRemindTerm(),
						new TimeValue().getMonth()),
					p.get(plan).getInfo()
				)
			)
			.toList();
	}
}
