package me.ajaja.module.plan.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RemindTime {
	MORNING(9),
	AFTERNOON(13),
	EVENING(22);

	private final int time;
}
