package me.ajaja.module.remind.application.model;

public record RemindAddress(
	Long userId,
	String remindType,
	String remindEmail,
	String phoneNumber
) {
}
