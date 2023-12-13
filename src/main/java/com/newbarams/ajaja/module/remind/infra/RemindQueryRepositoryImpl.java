package com.newbarams.ajaja.module.remind.infra;

import static com.newbarams.ajaja.module.remind.infra.QRemindEntity.*;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.RemindQueryRepository;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;
import com.newbarams.ajaja.module.remind.mapper.RemindMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class RemindQueryRepositoryImpl implements RemindQueryRepository {
	private final JPAQueryFactory queryFactory;
	private final RemindMapper remindMapper;
	private final RemindInfoMapper remindInfoMapper;

	public RemindResponse.CommonResponse findAllRemindByPlanId(Plan plan, List<Feedback> feedbacks) {
		Long planId = plan.getId();

		List<RemindEntity> reminds = queryFactory
			.selectFrom(remindEntity)
			.where(remindEntity.planId.eq(planId)
				.and(remindEntity.type.eq("PLAN")))
			.orderBy(remindEntity.createdAt.asc())
			.fetch();

		if (reminds.isEmpty()) {
			int lastRemindMonth = plan.getRemindTerm() == 1 ? 1 : 0;
			return createCommonResponse(Collections.emptyList(), lastRemindMonth, plan);
		}

		List<RemindResponse.Response> sentMessages
			= remindInfoMapper.toSentMessages(remindMapper.toDomain(reminds), feedbacks);

		return createCommonResponse(sentMessages, sentMessages.get(sentMessages.size() - 1).remindMonth(), plan);
	}

	private RemindResponse.CommonResponse createCommonResponse(
		List<RemindResponse.Response> responses,
		int remindMonth,
		Plan plan
	) {
		int sentRemindNumber = responses.size();
		int remindDate = plan.getRemindDate();
		int remindTerm = plan.getRemindTerm();

		for (int i = sentRemindNumber; i < plan.getTotalRemindNumber(); i++) {
			remindMonth += remindTerm;
			responses.add(remindInfoMapper.toFutureMessages(remindMonth, remindDate));
		}

		return new RemindResponse.CommonResponse(
			plan.getRemindTimeName(),
			plan.getRemindDate(),
			plan.getRemindTerm(),
			plan.getRemindTotalPeriod(),
			plan.getIsRemindable(),
			responses
		);
	}
}

