package me.ajaja.module.feedback.application;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.feedback.application.model.FeedbackPeriod;
import me.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.dto.FeedbackResponse;
import me.ajaja.module.feedback.infra.model.FeedbackInfo;
import me.ajaja.module.feedback.mapper.FeedbackMapper;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.remind.application.port.out.FindTargetPort;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadFeedbackInfoService {
	private final FeedbackQueryRepository feedbackQueryRepository;
	private final FindTargetPort findTargetPort;
	private final FeedbackMapper feedbackMapper;
	private final PlanMapper planMapper;

	public FeedbackResponse.FeedbackInfo loadFeedbackInfoByPlanId(Long userId, Long planId) {
		Plan plan = findTargetPort.findByUserIdAndPlanId(userId, planId);
		PlanFeedbackInfo planFeedbackInfo = planMapper.toModel(plan);

		List<FeedbackInfo> feedbackInfos = feedbackQueryRepository.findFeedbackInfosByPlanId(planId);

		List<FeedbackResponse.RemindFeedback> feedbacks =
			createRemindFeedbacks(planFeedbackInfo, feedbackInfos.iterator());

		return feedbackMapper.toResponse(planFeedbackInfo, feedbacks);
	}

	private List<FeedbackResponse.RemindFeedback> createRemindFeedbacks(
		PlanFeedbackInfo planFeedbackInfo,
		Iterator<FeedbackInfo> feedbackInfoIterator
	) {
		FeedbackInfo feedbackInfo = feedbackInfoIterator.hasNext() ? feedbackInfoIterator.next() : null;

		return planFeedbackInfo.periods().stream()
			.map(period -> createRemindFeedbackResponse(feedbackInfo, planFeedbackInfo, period, feedbackInfoIterator))
			.toList();
	}

	private FeedbackResponse.RemindFeedback createRemindFeedbackResponse(
		FeedbackInfo feedbackInfo,
		PlanFeedbackInfo planFeedbackInfo,
		FeedbackPeriod period,
		Iterator<FeedbackInfo> feedbackInfoIterator
	) {
		BaseTime sendDate = BaseTime.parse(
			planFeedbackInfo.createdYear(),
			period.remindMonth(),
			period.remindDate(),
			planFeedbackInfo.remindTime()
		);

		boolean isFeedbacked = isFeedbacked(feedbackInfo, planFeedbackInfo, period);

		if (isFeedbacked && feedbackInfoIterator.hasNext()) {
			feedbackInfoIterator.next();
		}

		return isFeedbacked
			? feedbackMapper.toResponse(sendDate, feedbackInfo, sendDate.oneMonthLater())
			: feedbackMapper.toEmptyResponse(sendDate, planFeedbackInfo.remindTime(), sendDate.oneMonthLater());
	}

	private boolean isFeedbacked(FeedbackInfo feedbackInfo, PlanFeedbackInfo planInfo, FeedbackPeriod feedbackPeriod) {
		if (feedbackInfo == null) {
			return false;
		}

		BaseTime feedbackDate = BaseTime.parse(
			planInfo.createdYear(),
			feedbackInfo.feedbackMonth(),
			feedbackInfo.feedbackDate(),
			planInfo.remindTime()
		);

		return feedbackDate.isWithinAMonth(BaseTime.parse(
			planInfo.createdYear(),
			feedbackPeriod.remindMonth(),
			feedbackPeriod.remindDate(),
			planInfo.remindTime())
		);
	}
}
