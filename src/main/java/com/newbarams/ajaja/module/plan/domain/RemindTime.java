package com.newbarams.ajaja.module.plan.domain;

import lombok.Getter;

@Getter
public enum RemindTime {
	MORNING(9),
	AFTERNOON(13),
	EVENING(22);

	private final int time;

	RemindTime(int time) {
		this.time = time;
	}
}
