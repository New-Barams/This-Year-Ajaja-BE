package com.newbarams.ajaja.module.remind.mapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.dto.GetRemindInfo;

@Component
public class RemindInfoMapper {
	public List<GetRemindInfo.SentRemindResponse> mapSentMessagesFrom(
		List<Remind> reminds,
		List<Feedback> feedbacks,
		TimeValue timeValue
	) {
		List<GetRemindInfo.SentRemindResponse> sentRemindResponses = new ArrayList<>();

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

			sentRemindResponses.add((new GetRemindInfo.SentRemindResponse(
				remind,
				feedback,
				remindMonth,
				remindDate,
				isFeedback,
				isExpired,
				endMonth,
				endDate
			)));
		}

		return sentRemindResponses;
	}

	public List<GetRemindInfo.FutureRemindResponse> mapFutureMessagesFrom(
		Plan plan,
		int sentRemindNumber,
		int lastRemindMonth
	) {
		List<GetRemindInfo.FutureRemindResponse> futureRemindResponses = new ArrayList<>();

		int remindTerm = plan.getInfo().getRemindTerm();
		int remindMonth = lastRemindMonth;
		int remindDate = plan.getInfo().getRemindDate();

		for (int i = sentRemindNumber; i < plan.getTotalRemindNumber(); i++) {
			remindMonth += remindTerm;

			futureRemindResponses.add(new GetRemindInfo.FutureRemindResponse(
				remindMonth,
				remindDate
			));
		}

		return futureRemindResponses;
	}
}
