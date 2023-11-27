package com.newbarams.ajaja.module.remind.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

@Component
public class RemindInfoMapper {
	public List<RemindResponse.SentResponse> mapSentMessagesFrom(
		List<Remind> reminds,
		List<Feedback> feedbacks
	) {
		List<RemindResponse.SentResponse> sentResponses = new ArrayList<>();

		for (int i = 0; i < reminds.size(); i++) {
			Remind remind = reminds.get(i);
			Feedback feedback = feedbacks.get(i);

			TimeValue startTimeValue = new TimeValue(remind.getStart());
			int remindMonth = startTimeValue.getMonth();
			int remindDate = startTimeValue.getDate();

			TimeValue endTimeValue = new TimeValue(remind.getEnd());
			int endMonth = endTimeValue.getMonth();
			int endDate = endTimeValue.getDate();

			boolean isFeedback = feedback.isFeedback();
			boolean isExpired = remind.isExpired();

			sentResponses.add((new RemindResponse.SentResponse(
				feedback.getId(),
				remind.getInfo().getContent(),
				remindMonth,
				remindDate,
				feedback.getRate(),
				isFeedback,
				isExpired,
				true,
				endMonth,
				endDate
			)));
		}

		return sentResponses;
	}

	public List<RemindResponse.FutureResponse> mapFutureMessagesFrom(
		Plan plan,
		int sentRemindNumber,
		int lastRemindMonth
	) {
		List<RemindResponse.FutureResponse> futureResponses = new ArrayList<>();

		int remindTerm = plan.getInfo().getRemindTerm();
		int remindMonth = lastRemindMonth;
		int remindDate = plan.getInfo().getRemindDate();

		for (int i = sentRemindNumber; i < plan.getTotalRemindNumber(); i++) {
			remindMonth += remindTerm;

			futureResponses.add(new RemindResponse.FutureResponse(
				0L,
				"",
				remindMonth,
				remindDate,
				0,
				false,
				false,
				false,
				0,
				0
			));
		}

		return futureResponses;
	}
}
