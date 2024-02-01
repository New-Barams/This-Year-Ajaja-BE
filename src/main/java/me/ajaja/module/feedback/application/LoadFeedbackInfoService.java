package me.ajaja.module.feedback.application;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.feedback.application.model.FeedbackPeriod;
import me.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.dto.FeedbackResponse;
import me.ajaja.module.feedback.infra.model.FeedbackInfo;
import me.ajaja.module.feedback.mapper.FeedbackMapper;
import me.ajaja.module.plan.application.LoadPlanService;

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
		FeedbackInfo feedbackInfo = feedbackInfos.hasNext() ? feedbackInfos.next() : null;

		return planInfo.periods().stream()
			.map(period -> createRemindFeedbackResponse(feedbackInfo, planInfo, period, feedbackInfos)).toList();
	}

	private FeedbackResponse.RemindFeedback createRemindFeedbackResponse(
		FeedbackInfo feedbackInfo,
		PlanFeedbackInfo planInfo,
		FeedbackPeriod period,
		Iterator<FeedbackInfo> feedbackInfos
	) {
		TimeValue sendDateValue = TimeValue.parse(planInfo.createdYear(), period.remindMonth(), period.remindDate(),
			planInfo.remindTime());

		boolean feedbacked = isFeedbacked(feedbackInfo, planInfo, period);
		if (feedbacked && feedbackInfos.hasNext()) {
			feedbackInfos.next();
		}

		return feedbacked ? feedbackMapper.toResponse(sendDateValue, feedbackInfo, sendDateValue.oneMonthLater()) :
			feedbackMapper.toEmptyResponse(sendDateValue, planInfo.remindTime(), sendDateValue.oneMonthLater());
	}

	private boolean isFeedbacked(FeedbackInfo feedbackInfo, PlanFeedbackInfo planInfo, FeedbackPeriod feedbackPeriod) {
		if (feedbackInfo == null) {
			return false;
		}

		TimeValue feedbackDate = TimeValue.parse(planInfo.createdYear(), feedbackInfo.feedbackMonth(),
			feedbackInfo.feedbackDate(), planInfo.remindTime());

		return feedbackDate.isBetween(TimeValue.parse(planInfo.createdYear(),
			feedbackPeriod.remindMonth(), feedbackPeriod.remindDate(), planInfo.remindTime()));
	}
}
