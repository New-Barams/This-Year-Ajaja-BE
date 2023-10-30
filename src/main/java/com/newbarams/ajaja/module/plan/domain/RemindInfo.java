package com.newbarams.ajaja.module.plan.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemindInfo extends SelfValidating<RemindInfo> {
	@Positive
	private int remindTotalPeriod;

	@Positive
	private int remindTerm;

	@Positive
	private int remindDate;

	public RemindInfo(int remindTotalPeriod, int remindTerm, int remindDate) {
		this.remindTotalPeriod = remindTotalPeriod;
		this.remindTerm = remindTerm;
		this.remindDate = remindDate;
		this.validateSelf();
	}
}
