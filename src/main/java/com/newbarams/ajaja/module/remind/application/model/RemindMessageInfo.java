package com.newbarams.ajaja.module.remind.application.model;

import com.newbarams.ajaja.module.plan.domain.Plan;

public record RemindMessageInfo(
	Plan plan,
	String email
) {
}
