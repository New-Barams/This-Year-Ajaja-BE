package com.newbarams.ajaja.module.plan.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemindInfo extends SelfValidating<RemindInfo> {
	enum RemindTime {
		MORNING,
		AFTERNOON,
		EVENING
	}

	@Positive
	private int remindTotalPeriod;

	@Positive
	private int remindTerm;

	@Positive
	private int remindDate;

	@Enumerated(value = EnumType.STRING)
	private RemindTime remindTime;

	public RemindInfo(int remindTotalPeriod, int remindTerm, int remindDate, RemindTime remindTime) {
		this.remindTotalPeriod = remindTotalPeriod;
		this.remindTerm = remindTerm;
		this.remindDate = remindDate;
		this.remindTime = remindTime;
		this.validateSelf();
	}
}
