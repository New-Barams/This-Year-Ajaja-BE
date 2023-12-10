package com.newbarams.ajaja.module.remind.infra;

import static com.newbarams.ajaja.module.remind.infra.QRemindEntity.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.RemindQueryRepository;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RemindQueryRepositoryImpl implements RemindQueryRepository {
	private final JPAQueryFactory queryFactory;
	private final RemindInfoMapper remindInfoMapper;

	public RemindResponse.CommonResponse findAllRemindByPlanId(Plan plan, List<Feedback> feedbacks) {
		Long planId = plan.getId();

		List<RemindEntity> reminds = queryFactory
			.selectFrom(remindEntity)
			.where(remindEntity.planId.eq(planId)
				.and(remindEntity.type.eq(RemindEntity.Type.PLAN)))
			.orderBy(remindEntity.createdAt.asc())
			.fetch();

		if (reminds.isEmpty()) {
			return createNoSentResponse(plan);
		}

		List<RemindResponse.SentResponse> sentMessages
			= remindInfoMapper.mapSentMessagesFrom(reminds, feedbacks);

		Instant lastRemindTime = reminds.get(sentMessages.size() - 1).getStarts();
		TimeValue timeValue = new TimeValue(lastRemindTime);
		int lastRemindMonth = timeValue.getMonth();

		List<RemindResponse.FutureResponse> futureMessages
			= remindInfoMapper.mapFutureMessagesFrom(plan, sentMessages.size(), lastRemindMonth);

		return new RemindResponse.CommonResponse(
			plan.getRemindTimeName(),
			plan.getRemindDate(),
			plan.getRemindTerm(),
			plan.getRemindTotalPeriod(),
			plan.getIsRemindable(),
			sentMessages,
			futureMessages
		);
	}

	private RemindResponse.CommonResponse createNoSentResponse(Plan plan) {
		int lastRemindMonth = plan.getRemindTerm() == 1 ? 1 : 0;
		List<RemindResponse.FutureResponse> futureResponses
			= remindInfoMapper.mapFutureMessagesFrom(plan, 0, lastRemindMonth);

		return new RemindResponse.CommonResponse(
			plan.getRemindTimeName(),
			plan.getRemindDate(),
			plan.getRemindTerm(),
			plan.getRemindTotalPeriod(),
			plan.getIsRemindable(),
			Collections.EMPTY_LIST,
			futureResponses
		);
	}
}
