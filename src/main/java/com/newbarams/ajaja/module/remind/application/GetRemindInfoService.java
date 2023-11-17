package com.newbarams.ajaja.module.remind.application;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.service.GetFeedbackService;
import com.newbarams.ajaja.module.plan.application.GetPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.dto.GetRemindInfo;
import com.newbarams.ajaja.module.remind.repository.RemindQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetRemindInfoService {
	private final RemindQueryRepository remindQueryRepository;
	private final GetFeedbackService getFeedbackService;
	private final GetPlanService getPlanService;

	public GetRemindInfo getRemindInfo(Long planId) {
		List<Remind> sentReminds = remindQueryRepository.findAllRemindByPlanId(planId);
		List<Feedback> feedbacks = getFeedbackService.getFeedback(planId);

		int sentRemindsNumber = sentReminds.size();

		List<GetRemindInfo.SentRemindResponse> sentRemindResponses
			= getPastRemindResponse(sentRemindsNumber, sentReminds, feedbacks);

		Plan plan = getPlanService.loadPlanOrElseThrow(planId);

		ZonedDateTime lastRemindTime = getDateTime(sentReminds.get(sentRemindsNumber).getPeriod().getStart());
		int totalRemindNumber = plan.getInfo().getTotalRemindNumber();

		List<GetRemindInfo.FutureRemindResponse> futureRemindResponses
			= getFutureRemindResponse(plan, totalRemindNumber, lastRemindTime);

		String remindTime = plan.getInfo().getRemindTime().name();

		return new GetRemindInfo.CommonResponse(
			plan.getInfo().getRemindTime(remindTime),
			plan.getInfo().getRemindDate(),
			plan.getInfo().getRemindTerm(),
			plan.getInfo().getRemindTotalPeriod(),
			plan.getStatus().isCanRemind(),
			sentRemindResponses,
			futureRemindResponses
		);
	}

	private List<GetRemindInfo.SentRemindResponse> getPastRemindResponse(int sentRemindsNumber,
		List<Remind> pastReminds, List<Feedback> feedbacks) {
		List<GetRemindInfo.SentRemindResponse> sentRemindResponses = new ArrayList<>();

		for (int i = 0; i < sentRemindsNumber; i++) {
			Remind remind = pastReminds.get(i);
			Feedback feedback = feedbacks.get(i);

			ZonedDateTime remindTime = getDateTime(remind.getPeriod().getStart());
			int remindMonth = remindTime.getMonthValue();
			int remindDate = remindTime.getDayOfMonth();

			ZonedDateTime expiredTime = getDateTime(remind.getPeriod().getEnd());
			int endMonth = expiredTime.getMonthValue();
			int endDate = expiredTime.getDayOfMonth();

			boolean isFeedback = feedback.getCreatedAt() == feedback.getUpdatedAt();
			boolean isExpired = remind.getPeriod().getEnd().isAfter(Instant.now());

			sentRemindResponses.add(new GetRemindInfo.SentRemindResponse(
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
				)
			);
		}

		return sentRemindResponses;
	}

	private ZonedDateTime getDateTime(Instant instant) {
		return instant.atZone(ZoneId.systemDefault());
	}

	private List<GetRemindInfo.FutureRemindResponse> getFutureRemindResponse(Plan plan, int totalRemindNumber,
		ZonedDateTime lastRemindTime) {
		List<GetRemindInfo.FutureRemindResponse> futureRemindResponses = new ArrayList<>();

		int remindTerm = plan.getInfo().getRemindTerm();
		int remindMonth = lastRemindTime.getDayOfMonth();
		int remindDate = plan.getInfo().getRemindDate();

		for (int i = 0; i < totalRemindNumber; i++) {
			remindMonth += remindTerm;

			new GetRemindInfo.FutureRemindResponse(
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
			);
		}

		return futureRemindResponses;
	}
}