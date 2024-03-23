package me.ajaja.module.feedback.application.model;

import me.ajaja.global.common.BaseTime;

public record UpdatableFeedback(
	String title,
	Long planId,
	BaseTime period
) {
}
