package me.ajaja.global.exception;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Exceptions {
	private static final String SUBLOG_PREFIX = "at ";
	private static final String NEW_LINE = "\n";

	private static final List<String> UNIMPORTANT_PACKAGES = List.of(
		"org.springframework.aop", "com.fasterxml.jackson",
		"java.base", "org.hibernate", "org.apache",
		"com.sun", "jakarta.servlet", "jdk.internal"
	);

	public static String simplifyMessage(Throwable throwable) {
		Objects.requireNonNull(throwable, "throwable cannot be null!");
		String stackTrace = ExceptionUtils.getStackTrace(throwable);
		return truncate(stackTrace);
	}

	private static String truncate(String message) {
		return Arrays.stream(message.split(NEW_LINE))
			.filter(Exceptions::isImportantLog)
			.collect(Collectors.joining(NEW_LINE));
	}

	private static boolean isImportantLog(String log) {
		return !(log.contains(SUBLOG_PREFIX) && containsAnyUnimportant(log));
	}

	private static boolean containsAnyUnimportant(String log) {
		return UNIMPORTANT_PACKAGES.stream().anyMatch(log::contains);
	}
}
