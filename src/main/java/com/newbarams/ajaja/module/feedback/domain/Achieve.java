package com.newbarams.ajaja.module.feedback.domain;

import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Achieve {
	FAIL(0),
	BAD(25),
	SOSO(50),
	GOOD(75),
	PERFECT(100);

	private final int rate;

	public static Achieve findAchieve(int rate) {
		return Stream.of(values())
			.filter(achieve -> achieve.rate == rate)
			.findAny()
			.orElseThrow(IllegalArgumentException::new);
	}
}
