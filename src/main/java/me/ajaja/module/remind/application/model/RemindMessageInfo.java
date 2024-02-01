package me.ajaja.module.remind.application.model;

import me.ajaja.module.plan.domain.Plan;

public record RemindMessageInfo(
	Plan plan,
	String remindType,
	String email,
	String phoneNumber
) {
}
