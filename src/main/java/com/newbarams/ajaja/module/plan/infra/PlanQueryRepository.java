package com.newbarams.ajaja.module.plan.infra;

import static com.newbarams.ajaja.module.ajaja.domain.Ajaja.Type.*;
import static com.newbarams.ajaja.module.ajaja.domain.QAjaja.*;
import static com.newbarams.ajaja.module.feedback.infra.QFeedbackEntity.*;
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
import com.newbarams.ajaja.module.plan.domain.RemindDate;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.dto.QPlanResponse_Detail;
import com.newbarams.ajaja.module.plan.dto.QPlanResponse_Writer;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.remind.application.model.RemindMessageInfo;
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

	private BooleanExpression isCurrentYear() {
		return planEntity.createdAt.year().eq(new TimeValue().getYear());
	}

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
		return asBoolean(queryFactory.selectFrom(ajaja)
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
				isEqualsYear(conditions.isCurrent()),
				cursorPagination(conditions))

			.orderBy(sortBy(conditions.getSort()))
			.limit(PAGE_SIZE)
			.fetch();

		return tupleToResponse(tuples);
	}

	private BooleanExpression isEqualsYear(boolean isNewYear) {
		int currentYear = new TimeValue().getYear();
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

	public List<PlanInfoResponse.GetPlan> findAllPlanByUserId(Long userId) {
		return queryFactory.select(Projections.constructor(PlanInfoResponse.GetPlan.class,
				planEntity.createdAt.year(),
				planEntity.id,
				planEntity.title,
				planEntity.canRemind,
				feedbackEntity.achieve.avg().intValue(),
				planEntity.iconNumber
			))
			.from(planEntity)
			.leftJoin(feedbackEntity).on(feedbackEntity.planId.eq(planEntity.id))
			.groupBy(planEntity.createdAt.year(),
				planEntity.id,
				planEntity.title,
				planEntity.canRemind,
				planEntity.iconNumber)
			.where(planEntity.userId.eq(userId))
			.orderBy(planEntity.createdAt.year().desc())
			.fetch();
	}

	public List<RemindMessageInfo> findAllRemindablePlan(String remindTime, TimeValue time) {
		return queryFactory.select(planEntity, userEntity.remindEmail)
			.from(planEntity)
			.join(userEntity).on(userEntity.id.eq(planEntity.userId))
			.where(planEntity.canRemind
				.and(planEntity.remindTime.eq(remindTime).and(isRemindable(time))))
			.fetch().stream()
			.map(t -> planMapper.toModel(t.get(planEntity), t.get(userEntity.remindEmail)))
			.toList();
	}

	private BooleanExpression isRemindable(TimeValue time) {
		RemindDate today = new RemindDate(time.getMonth(), time.getDate());
		return planEntity.createdAt.year().eq(time.getYear())
			.andAnyOf(planEntity.messages.any().remindDate.eq(today));
	}
}
