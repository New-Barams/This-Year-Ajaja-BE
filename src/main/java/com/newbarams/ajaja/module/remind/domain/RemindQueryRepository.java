package com.newbarams.ajaja.module.remind.domain;

import java.util.List;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

public interface RemindQueryRepository {
	RemindResponse.CommonResponse findAllRemindByPlanId(Plan plan, List<Feedback> feedbacks);
}
