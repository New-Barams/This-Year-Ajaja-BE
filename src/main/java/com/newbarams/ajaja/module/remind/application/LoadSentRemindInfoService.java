package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.application.LoadFeedbackService;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.RemindQueryRepository;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadSentRemindInfoService {
	private final LoadPlanService loadPlanService;
	private final LoadFeedbackService loadFeedbackService;
	private final RemindQueryRepository remindQueryRepository;

	public RemindResponse.CommonResponse loadRemindResponse(Long planId) {
		Plan plan = loadPlanService.loadPlanOrElseThrow(planId);
		List<Feedback> feedbacks = loadFeedbackService.loadFeedback(planId);
		return remindQueryRepository.findAllRemindByPlanId(plan, feedbacks);
	}
}
