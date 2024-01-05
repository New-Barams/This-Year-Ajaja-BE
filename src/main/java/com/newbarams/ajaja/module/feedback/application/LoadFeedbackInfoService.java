package com.newbarams.ajaja.module.feedback.application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.dto.FeedbackResponse;
import com.newbarams.ajaja.module.feedback.infra.model.FeedbackInfo;
import com.newbarams.ajaja.module.feedback.mapper.FeedbackMapper;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.RemindDate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadFeedbackInfoService {
	private final FeedbackQueryRepository feedbackQueryRepository;
	private final LoadPlanService loadPlanService;
	private final FeedbackMapper feedbackMapper;

	public FeedbackResponse.FeedbackInfo loadFeedbackInfoByPlanId(Long userId, Long planId) {
		PlanFeedbackInfo planFeedbackInfo = loadPlanService.loadPlanFeedbackInfoByPlanId(userId, planId);
		List<FeedbackInfo> feedbackInfos = feedbackQueryRepository.findFeedbackInfosByPlanId(planId);

		List<FeedbackResponse.RemindFeedback> feedbacks
			= createRemindFeedbacks(feedbackInfos.iterator(), planFeedbackInfo);

		return feedbackMapper.toResponse(planFeedbackInfo, feedbacks);
	}

	private List<FeedbackResponse.RemindFeedback> createRemindFeedbacks(
		Iterator<FeedbackInfo> feedbackInfos,
		PlanFeedbackInfo planInfo
	) {
		List<FeedbackResponse.RemindFeedback> feedbacks = new ArrayList<>();
		List<Message> messages = planInfo.messages();
		FeedbackInfo feedbackInfo = feedbackInfos.hasNext() ? feedbackInfos.next() : null;

		for (Message message : messages) {
			RemindDate sendDate = message.getRemindDate();

			FeedbackResponse.RemindFeedback feedback = createRemindFeedbackResponse(feedbackInfo, planInfo, sendDate,
				feedbackInfos);
			feedbacks.add(feedback);
		}
		return feedbacks;
	}

	private TimeValue parsePeriod(PlanFeedbackInfo planInfo, int month, int date) {
		return TimeValue.parseTimeValue(
			planInfo.createdYear(),
			month,
			date,
			planInfo.remindTime()
		);
	}

	private FeedbackResponse.RemindFeedback createRemindFeedbackResponse(
		FeedbackInfo feedbackInfo,
		PlanFeedbackInfo planInfo,
		RemindDate sendDate,
		Iterator<FeedbackInfo> infos
	) {
		TimeValue sendDateValue = parsePeriod(planInfo, sendDate.getRemindMonth(), sendDate.getRemindDay());

		return isFeedbacked(feedbackInfo, planInfo, sendDate, infos)
			? feedbackMapper.toResponse(sendDateValue, feedbackInfo, sendDateValue.oneMonthLater()) :
			feedbackMapper.toEmptyResponse(sendDateValue, planInfo.remindTime(), sendDateValue.oneMonthLater());
	}

	private boolean isFeedbacked(FeedbackInfo feedbackInfo, PlanFeedbackInfo planInfo, RemindDate sendDate,
		Iterator<FeedbackInfo> infos) {
		if (feedbackInfo == null) {
			return false;
		}

		TimeValue period = parsePeriod(planInfo, feedbackInfo.feedbackMonth(), feedbackInfo.feedbackDate());
		boolean isBetweenPeriod = period.isBetween(planInfo.createdYear(), sendDate.getRemindMonth(),
			sendDate.getRemindDay(), planInfo.remindTime());

		if (isBetweenPeriod && infos.hasNext()) {
			infos.next();
		}
		return isBetweenPeriod;
	}
}
