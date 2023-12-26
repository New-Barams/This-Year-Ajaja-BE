package com.newbarams.ajaja.module.plan.infra;

import static com.newbarams.ajaja.global.util.QueryDslUtil.*;
import static com.newbarams.ajaja.module.ajaja.domain.Ajaja.Type.*;
import static com.newbarams.ajaja.module.ajaja.domain.QAjaja.*;
import static com.newbarams.ajaja.module.plan.infra.QPlanEntity.*;
import static com.newbarams.ajaja.module.tag.domain.QPlanTag.*;
import static com.newbarams.ajaja.module.tag.domain.QTag.*;
import static com.newbarams.ajaja.module.user.infra.QUserEntity.*;
import static com.querydsl.core.types.dsl.Expressions.*;

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
import com.newbarams.ajaja.module.plan.dto.QPlanResponse_Detail;
import com.newbarams.ajaja.module.plan.dto.QPlanResponse_Writer;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.remind.dto.RemindMessageInfo;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlanQueryRepository {
	private static final String LATEST = "latest";
	private static final String AJAJA = "ajaja";
	private static final int PAGE_SIZE = 3;

	private final JPAQueryFactory queryFactory;
	private final PlanMapper planMapper;

	public List<Plan> findAllCurrentPlansByUserId(Long userId) { // todo: domain dependency
		return queryFactory.selectFrom(planEntity)
			.where(planEntity.userId.eq(userId)
				.and(isCurrentYear()))
			.fetch()
			.stream().map(planMapper::toDomain)
			.toList();
	}

	public Long countByUserId(Long userId) {
		return queryFactory.select(planEntity.count())
			.from(planEntity)
			.where(planEntity.userId.eq(userId)
				.and(isCurrentYear()))
			.fetchFirst();
	}

	public Optional<PlanResponse.Detail> findPlanDetailByIdAndOptionalUser(Long userId, Long id) {
		return Optional.ofNullable(queryFactory.select(new QPlanResponse_Detail(
				new QPlanResponse_Writer(
					userEntity.nickname,
					userId == null ? FALSE : userEntity.id.eq(userId),
					userId == null ? FALSE : isAjajaPressed(userId, id)),
				Expressions.asNumber(id),
				planEntity.title,
				planEntity.description,
				planEntity.iconNumber,
				planEntity.isPublic,
				planEntity.canRemind,
				planEntity.canAjaja,
				planEntity.ajajas.size().longValue(),
				Expressions.constant(findAllTagsByPlanId(id)),
				planEntity.createdAt))
			.from(planEntity)
			.leftJoin(userEntity).on(userEntity.id.eq(planEntity.userId))
			.where(planEntity.id.eq(id))
			.fetchOne()
		);
	}

	private BooleanExpression isAjajaPressed(Long userId, Long id) {
		return Expressions.asBoolean(queryFactory.selectFrom(ajaja)
			.where(ajaja.targetId.eq(id)
				.and(ajaja.userId.eq(userId))
				.and(ajaja.type.eq(Ajaja.Type.PLAN))
				.and(ajaja.isCanceled.isFalse()))
			.fetchFirst() != null);
	}

	public Optional<PlanResponse.GetOne> findById(Long id, Long userId) {
		List<Tuple> tuples = queryFactory.select(planEntity, userEntity.nickname)
			.from(planEntity, userEntity)
			.where(planEntity.userId.eq(userEntity.id).and(planEntity.id.eq(id)))
			.fetch();

		return getResponse(tuples, userId);
	}

	private Optional<PlanResponse.GetOne> getResponse(List<Tuple> tuples, Long userId) {
		return Optional.ofNullable(tupleToResponse(tuples.get(0), userId));
	}

	private PlanResponse.GetOne tupleToResponse(Tuple tuple, Long userId) {
		PlanEntity planFromTuple = tuple.get(planEntity);
		String nickname = tuple.get(userEntity.nickname);

		List<String> tags = findAllTagsByPlanId(planFromTuple.getId());
		boolean isPressAjaja = isPressAjaja(planFromTuple.getId(), userId);

		return planMapper.toResponse(planFromTuple, nickname, tags, isPressAjaja);
	}

	private boolean isPressAjaja(Long planId, Long userId) {
		return queryFactory.selectFrom(ajaja)
			.where(ajaja.targetId.eq(planId)
				.and(ajaja.userId.eq(userId))
				.and(ajaja.type.eq(PLAN))
				.and(ajaja.isCanceled.isFalse()))
			.fetchFirst() != null;
	}

	private List<String> findAllTagsByPlanId(Long planId) {
		return queryFactory.select(tag.name)
			.from(planTag, tag)
			.where(planTag.tagId.eq(tag.id)
				.and(planTag.planId.eq(planId)))
			.fetch();
	}

	public List<PlanResponse.GetAll> findAllByCursorAndSorting(PlanRequest.GetAll conditions) {
		List<Tuple> tuples = queryFactory.select(planEntity, userEntity.nickname)
			.from(planEntity, userEntity)

			.where(planEntity.userId.eq(userEntity.id),
				planEntity.isPublic.eq(true),
				isEqualsYear(conditions.current()),
				cursorPagination(conditions))

			.orderBy(sortBy(conditions.sort()))
			.limit(PAGE_SIZE)
			.fetch();

		return tupleToResponse(tuples);
	}

	private BooleanExpression isEqualsYear(boolean isNewYear) {
		int currentYear = new TimeValue().getYear();
		return isNewYear ? planEntity.createdAt.year().eq(currentYear) : planEntity.createdAt.year().ne(currentYear);
	}

	private BooleanExpression cursorPagination(PlanRequest.GetAll conditions) {
		return conditions.start() == null ? null :
			getCursorCondition(conditions.sort(), conditions.start(), conditions.ajaja());
	}

	private BooleanExpression getCursorCondition(String sort, Long start, Integer cursorAjaja) {
		return sort.equalsIgnoreCase(LATEST) ? cursorId(start) : cursorAjajaAndId(cursorAjaja, start);
	}

	private BooleanExpression cursorId(Long cursorId) {
		return planEntity.id.lt(cursorId);
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

	public List<PlanInfoResponse.GetPlan> findAllPlanByUserId(Long userId) {
		return queryFactory.select(Projections.constructor(PlanInfoResponse.GetPlan.class,
				planEntity.createdAt.year(),
				planEntity.id,
				planEntity.title,
				planEntity.canRemind,
				planEntity.achieveRate,
				planEntity.iconNumber,
				userEntity.verified
			))
			.from(planEntity)
			.join(userEntity).on(userEntity.id.eq(planEntity.userId))
			.groupBy(planEntity.createdAt.year(),
				planEntity.id,
				planEntity.title,
				planEntity.canRemind,
				planEntity.achieveRate,
				planEntity.iconNumber,
				userEntity.verified)
			.where(planEntity.userId.eq(userId))
			.orderBy(planEntity.createdAt.year().desc())
			.fetch();
	}

	public List<RemindMessageInfo> findAllRemindablePlan(String remindTime) {
		List<Tuple> remindablePlans = queryFactory.select(planEntity, userEntity)
			.from(planEntity, userEntity)
			.where(planEntity.canRemind
				.and(isCurrentYear())
				.and(isRemindMonth())
				.and(isRemindDate())
				.and(planEntity.remindTime.stringValue().eq(remindTime)
					.and(planEntity.userId.eq(userEntity.id)))
			)
			.fetch(); // todo: 리마인드 메세지 날짜에 맞게 쿼리 수정

		return mapRemindMessageInfos(remindablePlans);
	}

	private BooleanExpression isCurrentYear() {
		return planEntity.createdAt.year().eq(new TimeValue().getYear());
	}

	private BooleanExpression isRemindDate() {
		return planEntity.remindDate.eq(new TimeValue().getDate());
	}

	private List<RemindMessageInfo> mapRemindMessageInfos(List<Tuple> remindablePlans) {
		return remindablePlans.stream().map(
				p -> {
					Plan plan = planMapper.toDomain(p.get(planEntity));

					return new RemindMessageInfo(
						plan.getUserId(),
						plan.getId(),
						plan.getContent().getTitle(),
						p.get(userEntity).getRemindEmail(),
						plan.getMessage(plan.getRemindTerm(), new TimeValue().getMonth()),
						plan.getInfo(),
						plan.getRemindMonth(),
						plan.getRemindDate()
					);
				}
			)
			.toList();
	}
}
