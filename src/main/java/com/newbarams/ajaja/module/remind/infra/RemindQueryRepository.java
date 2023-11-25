package com.newbarams.ajaja.module.remind.infra;

import static com.newbarams.ajaja.module.remind.domain.QRemind.*;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.dto.GetRemindInfo;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RemindQueryRepository {
	private final JPAQueryFactory queryFactory;
	private final RemindInfoMapper remindInfoMapper;
	TimeValue timeValue = new TimeValue();

	public GetRemindInfo.CommonResponse findAllRemindByPlanId(Plan plan, List<Feedback> feedbacks) {
		Long planId = plan.getId();

		List<Remind> reminds = queryFactory
			.selectFrom(remind)
			.where(remind.planId.eq(planId)
				.and(remind.type.eq(Remind.Type.PLAN)))
			.orderBy(remind.createdAt.asc())
			.fetch();

		if (reminds.isEmpty()) {
			return getEmptyResponse(plan);
		}

		List<GetRemindInfo.SentRemindResponse> sentMessages = responseToSentMessages(reminds, feedbacks);

		int lastRemindMonth = timeValue.getMonthFrom(reminds.get(sentMessages.size() - 1).getPeriod().getStarts());

		List<GetRemindInfo.FutureRemindResponse> futureMessages
			= responseToFutureMessages(plan, sentMessages.size(), lastRemindMonth);

		return new GetRemindInfo.CommonResponse(plan, sentMessages, futureMessages);
	}

	private GetRemindInfo.CommonResponse getEmptyResponse(Plan plan) {
		int lastRemindMonth = plan.getRemindTerm() == 1 ? 1 : 0;
		List<GetRemindInfo.FutureRemindResponse> futureRemindResponses
			= responseToFutureMessages(plan, 0, lastRemindMonth);

		return new GetRemindInfo.CommonResponse(plan, Collections.EMPTY_LIST, futureRemindResponses);
	}

	private List<GetRemindInfo.SentRemindResponse> responseToSentMessages(
		List<Remind> reminds,
		List<Feedback> feedbacks
	) {
		return remindInfoMapper.mapSentMessagesFrom(reminds, feedbacks, timeValue);
	}

	private List<GetRemindInfo.FutureRemindResponse> responseToFutureMessages(
		Plan plan,
		int sentRemindNumber,
		int lastRemindMonth
	) {
		return remindInfoMapper.mapFutureMessagesFrom(plan, sentRemindNumber, lastRemindMonth);
	}
}
