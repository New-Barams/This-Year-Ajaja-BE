package com.newbarams.ajaja.module.plan.repository;

import static com.newbarams.ajaja.module.plan.domain.QPlan.*;
import static com.newbarams.ajaja.module.plan.exception.ErrorMessage.*;
import static com.newbarams.ajaja.module.user.domain.QUser.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlanRepositoryCustomImpl implements PlanRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public PlanResponse.GetOne findPlanById(Long id) {
		List<Tuple> tuples = queryFactory.select(plan, user.nickname)
			.from(plan, user)
			.where(plan.userId.eq(user.id).and(plan.id.eq(id)))
			.fetch();

		return validateAndGetResponse(tuples);
	}

	private PlanResponse.GetOne validateAndGetResponse(List<Tuple> tuples) {
		if (tuples.isEmpty()) {
			throw new NoSuchElementException(NOT_FOUND_PLAN.getMessage());
		}

		Tuple tuple = tuples.get(0);

		Plan planFromTuple = tuple.get(plan);
		String nickname = tuple.get(user.nickname).getNickname();

		return PlanMapper.toResponse(planFromTuple, nickname);
	}
}
