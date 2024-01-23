package com.newbarams.ajaja.module.remind.application.model;

public record RemindAddress(
	String userEmail,
	String type,
	String remindEmail,
	String phoneNumber
) {
}
