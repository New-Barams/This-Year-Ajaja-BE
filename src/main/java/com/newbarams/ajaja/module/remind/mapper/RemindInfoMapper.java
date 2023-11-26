package com.newbarams.ajaja.module.remind.mapper;

import java.time.Instant;
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
	public List<RemindResponse.SentRemindResponse> mapSentMessagesFrom(
		List<Remind> reminds,
		List<Feedback> feedbacks,
		TimeValue timeValue
	) {
		List<RemindResponse.SentRemindResponse> sentRemindResponses = new ArrayList<>();

		for (int i = 0; i < reminds.size(); i++) {
			Remind remind = reminds.get(i);
			Feedback feedback = feedbacks.get(i);

			Instant starts = remind.getPeriod().getStarts();
			int remindMonth = timeValue.getMonthFrom(starts);
			int remindDate = timeValue.getDateFrom(starts);

			Instant ends = remind.getPeriod().getEnds();
			int endMonth = timeValue.getMonthFrom(ends);
			int endDate = timeValue.getDateFrom(ends);

			boolean isFeedback = feedback.isFeedback();
			boolean isExpired = remind.isExpired();

			sentRemindResponses.add((new RemindResponse.SentRemindResponse(
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

		return sentRemindResponses;
	}

	public List<RemindResponse.FutureRemindResponse> mapFutureMessagesFrom(
		Plan plan,
		int sentRemindNumber,
		int lastRemindMonth
	) {
		List<RemindResponse.FutureRemindResponse> futureRemindResponses = new ArrayList<>();

		int remindTerm = plan.getInfo().getRemindTerm();
		int remindMonth = lastRemindMonth;
		int remindDate = plan.getInfo().getRemindDate();

		for (int i = sentRemindNumber; i < plan.getTotalRemindNumber(); i++) {
			remindMonth += remindTerm;

			futureRemindResponses.add(RemindResponse.FutureRemindResponse.of(
				remindMonth,
				remindDate
			));
		}

		return futureRemindResponses;
	}
}
