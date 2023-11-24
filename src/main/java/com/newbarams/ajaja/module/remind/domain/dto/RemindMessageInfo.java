package com.newbarams.ajaja.module.remind.domain.dto;

import com.newbarams.ajaja.module.plan.domain.RemindInfo;

import lombok.Getter;

@Getter
public class RemindMessageInfo {
	Long userId;
	Long planId;
	String email;
	String message;
	RemindInfo info;

	public RemindMessageInfo(Long userId, Long planId, String email, String message, RemindInfo info) {
		this.userId = userId;
		this.planId = planId;
		this.email = email;
		this.message = message;
		this.info = info;
	}
}
