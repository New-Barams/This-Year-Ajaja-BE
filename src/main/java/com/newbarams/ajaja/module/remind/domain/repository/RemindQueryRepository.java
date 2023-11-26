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
	TimeValue timeValue = new TimeValue();

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

		List<RemindResponse.SentRemindResponse> sentMessages = responseToSentMessages(reminds, feedbacks);

		Instant lastRemindTime = reminds.get(sentMessages.size() - 1).getLastRemindTime();
		int lastRemindMonth = timeValue.getMonthFrom(lastRemindTime);

		List<RemindResponse.FutureRemindResponse> futureMessages
			= responseToFutureMessages(plan, sentMessages.size(), lastRemindMonth);

		return RemindResponse.CommonResponse.of(plan, sentMessages, futureMessages);
	}

	private RemindResponse.CommonResponse createNoSentResponse(Plan plan) {
		int lastRemindMonth = plan.getRemindTerm() == 1 ? 1 : 0;
		List<RemindResponse.FutureRemindResponse> futureRemindResponses
			= responseToFutureMessages(plan, 0, lastRemindMonth);

		return RemindResponse.CommonResponse.of(plan, Collections.EMPTY_LIST, futureRemindResponses);
	}

	private List<RemindResponse.SentRemindResponse> responseToSentMessages(
		List<Remind> reminds,
		List<Feedback> feedbacks
	) {
		return remindInfoMapper.mapSentMessagesFrom(reminds, feedbacks, timeValue);
	}

	private List<RemindResponse.FutureRemindResponse> responseToFutureMessages(
		Plan plan,
		int sentRemindNumber,
		int lastRemindMonth
	) {
		return remindInfoMapper.mapFutureMessagesFrom(plan, sentRemindNumber, lastRemindMonth);
	}
}
