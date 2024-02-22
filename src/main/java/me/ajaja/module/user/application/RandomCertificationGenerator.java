package me.ajaja.module.user.application;

import java.security.SecureRandom;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class RandomCertificationGenerator {
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final int START_ASCII_CODE = 48;
	private static final int END_ASCII_CODE = 58;
	private static final int LENGTH = 6;

	public static String generate() {
		return RANDOM.ints(START_ASCII_CODE, END_ASCII_CODE)
			.limit(LENGTH)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	}
}
