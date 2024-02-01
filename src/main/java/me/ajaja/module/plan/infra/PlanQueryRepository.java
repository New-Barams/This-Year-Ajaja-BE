package me.ajaja.module.plan.infra;

import static com.querydsl.core.types.dsl.Expressions.*;
import static me.ajaja.global.exception.ErrorCode.*;
import static me.ajaja.module.plan.infra.QPlanEntity.*;
import static me.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.infra.QAjajaEntity;
import me.ajaja.module.feedback.infra.QFeedbackEntity;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.RemindDate;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.dto.QPlanResponse_Detail;
import me.ajaja.module.plan.dto.QPlanResponse_Writer;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.remind.application.model.RemindMessageInfo;
import me.ajaja.module.tag.domain.QPlanTag;
import me.ajaja.module.tag.domain.QTag;

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
		return planEntity.createdAt.year().eq(TimeValue.now().getYear());
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
		return Expressions.asBoolean(queryFactory.selectFrom(QAjajaEntity.ajajaEntity)
			.where(QAjajaEntity.ajajaEntity.targetId.eq(id)
				.and(QAjajaEntity.ajajaEntity.userId.eq(userId))
				.and(QAjajaEntity.ajajaEntity.type.eq(Ajaja.Type.PLAN.name()))
				.and(QAjajaEntity.ajajaEntity.canceled.isFalse()))
			.fetchFirst() != null);
	}

	public Plan findByUserIdAndPlanId(Long userId, Long planId) {
		PlanEntity entity = queryFactory.selectFrom(planEntity)
			.where(planEntity.userId.eq(userId)
				.and(planEntity.id.eq(planId)))
			.fetchOne();

		if (entity == null) {
			throw AjajaException.withId(planId, NOT_FOUND_PLAN);
		}
		return planMapper.toDomain(entity);
	}

	private List<String> findAllTagsByPlanId(Long planId) {
		return queryFactory.select(QTag.tag.name)
			.from(QPlanTag.planTag, QTag.tag)
			.where(QPlanTag.planTag.tagId.eq(QTag.tag.id)
				.and(QPlanTag.planTag.planId.eq(planId)))
			.fetch();
	}

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

	public List<PlanResponse.PlanInfo> findAllPlanByUserId(Long userId) {
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

	public List<RemindMessageInfo> findAllRemindablePlan(String remindTime, String remindType, TimeValue time) {
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
			.toList();
	}

	private BooleanExpression isRemindable(TimeValue time) {
		RemindDate today = new RemindDate(time.getMonth(), time.getDate());
		return planEntity.createdAt.year().eq(time.getYear())
			.andAnyOf(planEntity.messages.any().remindDate.eq(today));
	}
}
