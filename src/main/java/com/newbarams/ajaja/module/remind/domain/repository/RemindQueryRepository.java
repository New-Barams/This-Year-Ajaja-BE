package com.newbarams.ajaja.module.remind.domain.repository;

import static com.newbarams.ajaja.module.remind.domain.QRemind.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RemindQueryRepository {
	private final JPAQueryFactory queryFactory;
	private final RemindInfoMapper remindInfoMapper;

	public RemindResponse.CommonResponse findAllRemindByPlanId(Plan plan, List<Feedback> feedbacks) {
		Long planId = plan.getId();

		List<Remind> reminds = queryFactory
			.selectFrom(remind)
			.where(remind.planId.eq(planId)
				.and(remind.type.eq(Remind.Type.PLAN)))
			.orderBy(remind.createdAt.asc())
			.fetch();

		if (reminds.isEmpty()) {
			return createNoSentResponse(plan);
		}

		List<RemindResponse.SentRemindResponse> sentMessages
			= remindInfoMapper.mapSentMessagesFrom(reminds, feedbacks);

		Instant lastRemindTime = reminds.get(sentMessages.size() - 1).getStart();
		TimeValue timeValue = new TimeValue(lastRemindTime);
		int lastRemindMonth = timeValue.getMonth();

		List<RemindResponse.FutureRemindResponse> futureMessages
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
		List<RemindResponse.FutureRemindResponse> futureRemindResponses
			= remindInfoMapper.mapFutureMessagesFrom(plan, 0, lastRemindMonth);

		return new RemindResponse.CommonResponse(
			plan.getRemindTimeName(),
			plan.getRemindDate(),
			plan.getRemindTerm(),
			plan.getRemindTotalPeriod(),
			plan.getIsRemindable(),
			Collections.EMPTY_LIST,
			futureRemindResponses
		);
	}
}
