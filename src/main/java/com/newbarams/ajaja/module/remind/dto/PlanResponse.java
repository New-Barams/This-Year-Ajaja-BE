package com.newbarams.ajaja.module.remind.dto;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class PlanResponse {
	Plan plan;
	Remind remind;
	Feedback feedback;

	@QueryProjection
	public PlanResponse(Plan plan, Remind remind, Feedback feedback) {
		this.plan = plan;
		this.remind = remind;
		this.feedback = feedback;
	}
}
