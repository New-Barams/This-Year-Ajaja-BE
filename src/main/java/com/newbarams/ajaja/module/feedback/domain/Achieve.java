package com.newbarams.ajaja.module.feedback.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Achieve {
	FAIL(0),
	BAD(25),
	SOSO(50),
	GOOD(75),
	PERFECT(100);

	private final int rate;
}
