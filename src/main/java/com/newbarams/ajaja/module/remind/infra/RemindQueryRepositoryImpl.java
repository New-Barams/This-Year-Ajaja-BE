package com.newbarams.ajaja.module.remind.infra;

import static com.newbarams.ajaja.module.remind.infra.QRemindEntity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.RemindQueryRepository;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class RemindQueryRepositoryImpl implements RemindQueryRepository {
	private final JPAQueryFactory queryFactory;
	private final RemindInfoMapper remindInfoMapper;

	public RemindResponse.CommonResponse findAllReminds(Plan plan) {
		Long planId = plan.getId();

		List<RemindEntity> reminds = queryFactory
			.selectFrom(remindEntity)
			.where(remindEntity.planId.eq(planId)
				.and(remindEntity.type.eq("PLAN")))
			.orderBy(remindEntity.createdAt.asc())
			.fetch();

		return createCommonResponse(
			reminds.isEmpty() ? new ArrayList<>() : remindInfoMapper.toSentMessages(reminds),
			plan
		);
	}

	private RemindResponse.CommonResponse createCommonResponse(
		List<RemindResponse.Messages> responses,
		Plan plan
	) {
		List<Message> messages = plan.getMessages();

		IntStream.range(responses.size(), plan.getTotalRemindNumber()).mapToObj(i ->
			responses.add(remindInfoMapper.toFutureMessages(messages.get(i)))
		).toList();

		return new RemindResponse.CommonResponse(
			plan.getRemindTimeName(),
			plan.getIsRemindable(),
			plan.getRemindTotalPeriod(),
			plan.getRemindTerm(),
			plan.getRemindDate(),
			responses
		);
	}
}

