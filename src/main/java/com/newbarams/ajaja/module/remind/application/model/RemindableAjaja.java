package com.newbarams.ajaja.module.remind.application.model;

public record RemindableAjaja(
	String title,
	Long planId,
	Long userId,
	Long count,
	String email
) {
}
