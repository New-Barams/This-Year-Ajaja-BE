package com.newbarams.ajaja.module.plan.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemindDate {
	private int remindMonth;
	private int remindDay;

	public RemindDate(int remindMonth, int remindDay) {
		this.remindMonth = remindMonth;
		this.remindDay = remindDay;
	}
}
